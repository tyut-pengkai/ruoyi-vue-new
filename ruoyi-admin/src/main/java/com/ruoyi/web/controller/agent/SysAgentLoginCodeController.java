package com.ruoyi.web.controller.agent;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.domain.SysAgentItem;
import com.ruoyi.agent.service.ISysAgentItemService;
import com.ruoyi.agent.service.ISysAgentUserService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BalanceChangeType;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.TemplateType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.system.domain.SysLoginCode;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.domain.vo.BalanceChangeVo;
import com.ruoyi.system.domain.vo.BatchNoVo;
import com.ruoyi.system.service.ISysBalanceLogService;
import com.ruoyi.system.service.ISysLoginCodeService;
import com.ruoyi.system.service.ISysLoginCodeTemplateService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 单码Controller
 *
 * @author zwgu
 * @date 2021-12-03
 */
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
    private ISysBalanceLogService sysBalanceLogService;

    /**
     * 查询单码列表
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLoginCode sysLoginCode) {
        startPage();
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            sysLoginCode.setAgentId(getUserId());
        }
        List<SysLoginCode> list = sysLoginCodeService.selectSysLoginCodeList(sysLoginCode);
        return getDataTable(list);
    }

    /**
     * 导出单码列表
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:export')")
    @Log(title = "单码", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysLoginCode sysLoginCode) {
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            sysLoginCode.setAgentId(getUserId());
        }
        List<SysLoginCode> list = sysLoginCodeService.selectSysLoginCodeList(sysLoginCode);
        ExcelUtil<SysLoginCode> util = new ExcelUtil<SysLoginCode>(SysLoginCode.class);
        return util.exportExcel(list, "单码数据");
    }

    /**
     * 获取单码详细信息
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:query')")
    @GetMapping(value = "/{cardId}")
    public AjaxResult getInfo(@PathVariable("cardId") Long cardId) {
        return AjaxResult.success(sysLoginCodeService.selectSysLoginCodeByCardId(cardId));
    }

    /**
     * 新增单码
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:add')")
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
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
            // 判断是否有代理权限
            sysAgentService.checkAgent(agent, false);
            // 判断是否有代理该卡的权限
            SysAgentItem agentItem = sysAgentItemService.checkAgentItem(null, agent.getAgentId(), TemplateType.LOGIN_CODE, sysLoginCode.getTemplateId());
            // 计算金额
            BigDecimal unitPrice = agentItem.getAgentPrice();
            // 获取代理价格
            if (unitPrice == null) {
                throw new ServiceException("商品未设定价格", 400);
            }
            BigDecimal totalFee = unitPrice.multiply(BigDecimal.valueOf(sysLoginCode.getGenQuantity()));
            // 扣除余额
            BalanceChangeVo change = new BalanceChangeVo();
            change.setUserId(getUserId());
            change.setUpdateBy(getUsername());
            change.setType(BalanceChangeType.CONSUME);
            change.setDescription("批量制卡：[" + sysLoginCodeTemplate.getApp().getAppName() + "]" + sysLoginCodeTemplate.getCardName() + "，" + sysLoginCode.getGenQuantity() + "张，单价" + unitPrice);
            change.setAvailablePayBalance(totalFee.negate());
            // 扣款
            userService.updateUserBalance(change);
        }
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
    @Log(title = "单码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysLoginCode sysLoginCode) {
        sysLoginCode.setUpdateBy(getUsername());
        return toAjax(sysLoginCodeService.updateSysLoginCode(sysLoginCode));
    }

    /**
     * 删除单码
     */
    @PreAuthorize("@ss.hasPermi('agent:agentLoginCode:remove')")
    @Log(title = "单码", businessType = BusinessType.DELETE)
    @DeleteMapping("/{cardIds}")
    public AjaxResult remove(@PathVariable Long[] cardIds) {
        return toAjax(sysLoginCodeService.deleteSysLoginCodeByCardIds(cardIds));
    }

    /**
     * 查询单码类别列表
     */
    @PreAuthorize("@ss.hasAnyRoles('agent,admin,sadmin')")
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
    @GetMapping("/selectBatchNoList")
    public AjaxResult selectBatchNoList() {
        Long agentId = null;
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            agentId = getUserId();
        }
        List<BatchNoVo> batchNoList = sysLoginCodeService.selectBatchNoList(agentId);
        return AjaxResult.success(batchNoList);
    }
}
