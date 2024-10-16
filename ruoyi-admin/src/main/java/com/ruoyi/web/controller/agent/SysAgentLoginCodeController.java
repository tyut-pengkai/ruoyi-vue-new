package com.ruoyi.web.controller.agent;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.domain.SysAgentItem;
import com.ruoyi.agent.service.ISysAgentItemService;
import com.ruoyi.agent.service.ISysAgentUserService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.*;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.agent.anno.AgentPermCheck;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.domain.vo.BalanceChangeVo;
import com.ruoyi.system.domain.vo.BatchNoVo;
import com.ruoyi.system.domain.vo.BatchReplaceVo;
import com.ruoyi.system.service.ISysLoginCodeService;
import com.ruoyi.system.service.ISysLoginCodeTemplateService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.utils.poi.ExcelUtil;
import com.ruoyi.web.controller.system.SysLoginCodeController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 单码Controller
 *
 * @author zwgu
 * @date 2021-12-03
 */
@Slf4j
@RestController
@RequestMapping("/agent/agentLoginCode")
public class SysAgentLoginCodeController extends BaseController {
    @Autowired
    private ISysLoginCodeService sysLoginCodeService;
    @Resource
    private ISysAgentItemService sysAgentItemService;
    @Resource
    private ISysAgentUserService sysAgentService;
    @Resource
    private ISysLoginCodeTemplateService sysLoginCodeTemplateService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private ISysUserService userService;
    @Resource
    private SysLoginCodeController loginCodeController;

    /**
     * 查询单码列表
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:list')")
    @AgentPermCheck
    @GetMapping("/list")
    public TableDataInfo list(SysLoginCode sysLoginCode) {
//        if (!permissionService.hasAnyRoles("sadmin,admin")) {
        SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
        if(agent == null) {
            throw new ServiceException("您没有该操作的权限（代理系统）");
        }
        sysLoginCode.setAgentId(agent.getAgentId());
//        }
        startPage();
        List<SysLoginCode> list = sysLoginCodeService.selectSysLoginCodeList(sysLoginCode);
        return getDataTable(list);
    }

    /**
     * 导出单码列表
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:export')")
    @AgentPermCheck
    @Log(title = "单码", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysLoginCode sysLoginCode) {
//        if (!permissionService.hasAnyRoles("sadmin,admin")) {
        SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
        if(agent == null) {
            throw new ServiceException("您没有该操作的权限（代理系统）");
        }
        sysLoginCode.setAgentId(agent.getAgentId());
//        }
        List<SysLoginCode> list = sysLoginCodeService.selectSysLoginCodeList(sysLoginCode);
        ExcelUtil<SysLoginCode> util = new ExcelUtil<SysLoginCode>(SysLoginCode.class);
        return util.exportExcel(list, "单码数据");
    }

    /**
     * 获取单码详细信息
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:query')")
    @AgentPermCheck
    @GetMapping(value = "/{cardId}")
    public AjaxResult getInfo(@PathVariable("cardId") Long cardId) {
        return AjaxResult.success(sysLoginCodeService.selectSysLoginCodeByCardId(cardId));
    }

    /**
     * 新增单码
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:add')")
    @AgentPermCheck("enableAddCard")
    @Log(title = "单码", businessType = BusinessType.INSERT)
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult add(@RequestBody SysLoginCode sysLoginCode) {
        if (sysLoginCode.getTemplateId() == null) {
            return AjaxResult.error("未指定卡类，批量制卡失败");
        }
        if (sysLoginCode.getGenQuantity() == null || sysLoginCode.getGenQuantity() < 0) {
            return AjaxResult.error("制卡数量有误，批量制卡失败");
        }
        SysLoginCodeTemplate sysLoginCodeTemplate = sysLoginCodeTemplateService.selectSysLoginCodeTemplateByTemplateId(sysLoginCode.getTemplateId());
        if (sysLoginCodeTemplate == null) {
            return AjaxResult.error("卡类不存在，批量制卡失败");
        }

        SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
        BigDecimal unitPrice;
        String username = SecurityUtils.getUsernameNoException();
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            // 判断是否有代理该卡的权限
            SysAgentItem agentItem = sysAgentItemService.checkAgentItem(null, agent.getAgentId(), TemplateType.LOGIN_CODE, sysLoginCode.getTemplateId());
            // 计算金额
            unitPrice = agentItem.getAgentPrice();
            // 获取代理价格
            if (unitPrice == null) {
                throw new ServiceException("商品未设定价格", 400);
            }
            BigDecimal totalFee = unitPrice.multiply(BigDecimal.valueOf(sysLoginCode.getGenQuantity()));
            synchronized (SysAgentLoginCodeController.class) {
                // 扣除余额
                BalanceChangeVo change = new BalanceChangeVo();
                change.setUserId(getUserId());
                change.setUpdateBy(username);
                change.setType(BalanceChangeType.CONSUME);
                change.setDescription("批量制卡：[" + sysLoginCodeTemplate.getApp().getAppName() + "]" + sysLoginCodeTemplate.getCardName() + "，" + sysLoginCode.getGenQuantity() + "张，单价" + unitPrice + "元");
                change.setAvailablePayBalance(totalFee.negate());
                // 扣款
                userService.updateUserBalance(change);
            }
        } else {
            unitPrice = null;
        }
        // 利润分成
        new Thread(() -> {
            try {
                // 获取代理路径
                String path = agent.getPath();
                List<String> agentIdList = Arrays.stream(path.split("/")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
                SysAgent sAgent = null;
                SysAgentItem sAgentItem = null;
                for (int i = agentIdList.size() - 1; i > 0; i--) {
                    Long sAgentId = Long.parseLong(agentIdList.get(i));
                    Long pAgentId = Long.parseLong(agentIdList.get(i - 1));
                    if (sAgent == null) {
                        sAgent = sysAgentService.selectSysAgentByAgentId(sAgentId);
                    }
                    SysAgent pAgent = sysAgentService.selectSysAgentByAgentId(pAgentId);
                    if (sAgent == null || pAgent == null) {
                        log.error("代理分成失败，子代理：{}，父代理：{}，卡类：{}", sAgent != null ? (sAgent.getUser().getNickName() + "(" + sAgent.getUser().getUserName() + ")") : null,
                                pAgent != null ? (pAgent.getUser().getNickName() + "(" + pAgent.getUser().getUserName() + ")") : null,
                                "[" + sysLoginCodeTemplate.getApp().getAppName() + "]" + sysLoginCodeTemplate.getCardName());
                        continue;
                    }
                    // 获取子代理价格
                    SysAgentItem pAgentItem = null;
                    try {
                        if (sAgentItem == null) {
                            sAgentItem = sysAgentItemService.checkAgentItem(null, sAgent.getAgentId(), TemplateType.LOGIN_CODE, sysLoginCode.getTemplateId());
                        }
                        pAgentItem = sysAgentItemService.checkAgentItem(null, pAgent.getAgentId(), TemplateType.LOGIN_CODE, sysLoginCode.getTemplateId());
                    } catch (ServiceException e) {
                        sAgent = null;
                        sAgentItem = null;
                        log.error("代理分成失败，子代理id：{}，父代理id：{}，卡类：{}", sAgentId, pAgentId, "[" + sysLoginCodeTemplate.getApp().getAppName() + "]" + sysLoginCodeTemplate.getCardName(), e);
                        continue;
                    }
                    // 计算差价即利润
                    if (sAgentItem != null && pAgentItem != null && sAgentItem.getAgentPrice() != null && pAgentItem.getAgentPrice() != null) {
                        BigDecimal profit = sAgentItem.getAgentPrice().subtract(pAgentItem.getAgentPrice());
                        if (profit.compareTo(BigDecimal.ZERO) > 0) {
                            BigDecimal totalFee = profit.multiply(BigDecimal.valueOf(sysLoginCode.getGenQuantity()));
                            synchronized (SysAgentLoginCodeController.class) {
                                // 增加余额
                                BalanceChangeVo change = new BalanceChangeVo();
                                change.setUserId(pAgent.getUserId());
                                change.setUpdateBy(username);
                                change.setType(BalanceChangeType.AGENT);
                                change.setDescription("子代理[" + sAgent.getUser().getNickName() + "(" + sAgent.getUser().getUserName() + ")]" +
                                        "批量制卡：[" + sysLoginCodeTemplate.getApp().getAppName() + "]" + sysLoginCodeTemplate.getCardName() + "，" + sysLoginCode.getGenQuantity() + "张，进价：" + pAgentItem.getAgentPrice() + "元，售价：" + unitPrice + "元，" +
                                        "，每张分成" + profit + "元");
                                change.setAvailablePayBalance(totalFee);
                                // 扣款
                                userService.updateUserBalance(change);
                            }
                        }
                        sAgent = pAgent;
                        sAgentItem = pAgentItem;
                    } else {
                        sAgent = null;
                        sAgentItem = null;
                        log.error("代理分成失败，子代理id：{}，父代理id：{}，卡类：{}", sAgentId, pAgentId, "[" + sysLoginCodeTemplate.getApp().getAppName() + "]" + sysLoginCodeTemplate.getCardName());
                    }
                }
            } catch(Exception e) {
                log.error("代理分成异常", e);
            }
        }).start();
        List<SysLoginCode> sysLoginCodes = sysLoginCodeTemplateService.genSysLoginCodeBatch(sysLoginCodeTemplate, sysLoginCode.getGenQuantity(), UserConstants.NO, UserConstants.YES, sysLoginCode.getRemark());
        List<Map<String, String>> resultList = new ArrayList<>();
        for (SysLoginCode item : sysLoginCodes) {
            Map<String, String> map = new HashMap<>();
            map.put("cardNo", item.getCardNo());
            map.put("expireTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, item.getExpireTime()));
            resultList.add(map);
        }
        return AjaxResult.success("生成完毕", resultList).put("cardName", sysLoginCodeTemplate.getCardName());
    }

    /**
     * 修改单码
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:edit')")
    @AgentPermCheck
    @Log(title = "单码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysLoginCode sysLoginCode) {
        SysLoginCode card = sysLoginCodeService.selectSysLoginCodeByCardId(sysLoginCode.getCardId());
        // 检查是否有变更面值权限
        if(sysLoginCode.getQuota() != null && !Objects.equals(sysLoginCode.getQuota(), card.getQuota())) {
            if(card.getApp().getBillType() == BillType.TIME) {
                if(!permissionService.hasAgentPermi("enableUpdateCardTime")) {
                    throw new ServiceException("您没有该操作的权限（代理系统）");
                }
            } else if(card.getApp().getBillType() == BillType.POINT) {
                if(!permissionService.hasAgentPermi("enableUpdateCardPoint")) {
                    throw new ServiceException("您没有该操作的权限（代理系统）");
                }
            }
        }
        // 检查是否有变更状态权限
        if(sysLoginCode.getStatus() != null && !Objects.equals(sysLoginCode.getStatus(), card.getStatus())) {
            if (Objects.equals(sysLoginCode.getStatus(), UserStatus.OK.getCode())) {
                if (!permissionService.hasAgentPermi("enableUpdateCardStatus0")) {
                    throw new ServiceException("您没有该操作的权限（代理系统）");
                }
            }
            if (Objects.equals(sysLoginCode.getStatus(), UserStatus.DISABLE.getCode())) {
                if (!permissionService.hasAgentPermi("enableUpdateCardStatus1")) {
                    throw new ServiceException("您没有该操作的权限（代理系统）");
                }
            }
        }
        // 检查是否有变更权限
        checkAgentEditCardPerm(sysLoginCode, card);
        sysLoginCode.setUpdateBy(getUsername());
        return toAjax(sysLoginCodeService.updateSysLoginCode(sysLoginCode));
    }

    private void checkAgentEditCardPerm(SysLoginCode loginCode, SysLoginCode oCard) {
        Map<String, String> map =  new HashMap<>();
        map.put("CardLoginLimitU", "enableUpdateCardLoginLimitU");
        map.put("CardLoginLimitM", "enableUpdateCardLoginLimitM");
        map.put("CardCustomParams", "enableUpdateCardCustomParams");
        map.put("Remark", "enableUpdateCardRemark");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            try {
                Method declaredMethod = SysAppUser.class.getDeclaredMethod("get" + StringUtils.capitalize(entry.getKey()));
                Object value = declaredMethod.invoke(loginCode);
                Object oValue = declaredMethod.invoke(oCard);
                if (value != null && !Objects.equals(value, oValue) && !permissionService.hasAgentPermi(entry.getValue())) {
                    throw new ServiceException("您没有该操作的权限（代理系统）");
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new ServiceException(e.getMessage());
            }
        }
    }

    /**
     * 删除单码
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:remove')")
    @AgentPermCheck("enableDeleteCard")
    @Log(title = "单码", businessType = BusinessType.DELETE)
    @DeleteMapping("/{cardIds}")
    public AjaxResult remove(@PathVariable Long[] cardIds) {
        return toAjax(sysLoginCodeService.deleteSysLoginCodeByCardIds(cardIds));
    }

    /**
     * 查询单码类别列表
     */
    @PreAuthorize("@ss.hasAnyRoles('agent,admin,sadmin')")
    @AgentPermCheck
    @GetMapping("/loginCodeTemplate/listAll")
    public TableDataInfo listAll(SysLoginCodeTemplate sysLoginCodeTemplate) {
        List<SysLoginCodeTemplate> list = new ArrayList<>();
        if (sysLoginCodeTemplate.getAppId() != null) {
            if (!permissionService.hasAnyRoles("sadmin,admin")) {
                Set<Long> appIds = new HashSet<>();
                // 获取我的代理ID
                SysAgent agent = sysAgentService.selectSysAgentByUserId(getLoginUser().getUserId());
                if (agent != null) {
                    // 获取代理的所有卡类
                    List<SysAgentItem> agentItems = sysAgentItemService.selectSysAgentItemByAgentId(agent.getAgentId());
                    // 获取所有卡类信息
                    for (SysAgentItem item : agentItems) {
                        if (item.getTemplateType() == TemplateType.LOGIN_CODE) {
                            SysLoginCodeTemplate template = sysLoginCodeTemplateService.selectSysLoginCodeTemplateByTemplateId(item.getTemplateId());
                            if (sysLoginCodeTemplate.getAppId() != null && !Objects.equals(template.getAppId(), sysLoginCodeTemplate.getAppId())) {
                                continue;
                            }
                            if (!appIds.contains(template.getTemplateId())) {
                                template.setAgentPrice(item.getAgentPrice());
                                list.add(template);
                                appIds.add(template.getTemplateId());
                            }
                        }
                    }
                }
            } else {
                List<SysLoginCodeTemplate> templates = sysLoginCodeTemplateService.selectSysLoginCodeTemplateList(sysLoginCodeTemplate);
                for (SysLoginCodeTemplate template : templates) {
                    template.setAgentPrice(BigDecimal.ZERO);
                }
                list.addAll(templates);
            }
        }
        return getDataTable(list);
    }

    /**
     * 获取批次号列表
     *
     * @return
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:list')")
    @AgentPermCheck
    @GetMapping("/selectBatchNoList")
    public AjaxResult selectBatchNoList() {
        Long agentId = null;
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            agentId = getUserId();
        }
        List<BatchNoVo> batchNoList = sysLoginCodeService.selectBatchNoList(agentId);
        return AjaxResult.success(batchNoList);
    }

    /**
     * 批量换卡
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:replace')")
    @Log(title = "单码", businessType = BusinessType.REPLACE)
    @PostMapping("/batchReplace")
    public AjaxResult batchReplace(@RequestBody BatchReplaceVo vo) {
        if(!Objects.equals(UserConstants.NO, vo.getChangeMode())) {
            vo.setChangeMode(UserConstants.NO); // 代理只能换残卡
        }
        return loginCodeController.batchReplace(vo);
    }
}
