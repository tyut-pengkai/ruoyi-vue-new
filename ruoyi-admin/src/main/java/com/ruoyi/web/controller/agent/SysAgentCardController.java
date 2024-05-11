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
import com.ruoyi.framework.license.anno.AgentPermCheck;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.domain.vo.BalanceChangeVo;
import com.ruoyi.system.domain.vo.BatchNoVo;
import com.ruoyi.system.service.ISysBalanceLogService;
import com.ruoyi.system.service.ISysCardService;
import com.ruoyi.system.service.ISysCardTemplateService;
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
 * 卡密Controller
 *
 * @author zwgu
 * @date 2021-12-03
 */
@RestController
@RequestMapping("/agent/agentCard")
public class SysAgentCardController extends BaseController {
    @Autowired
    private ISysCardService sysCardService;
    @Resource
    private ISysAgentItemService sysAgentItemService;
    @Resource
    private ISysAgentUserService sysAgentService;
    @Resource
    private ISysCardTemplateService sysCardTemplateService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private ISysUserService userService;
    @Resource
    private ISysBalanceLogService sysBalanceLogService;

    /**
     * 查询卡密列表
     */
    @PreAuthorize("@ss.hasPermi('agent:agentCard:list')")
    @AgentPermCheck
    @GetMapping("/list")
    public TableDataInfo list(SysCard sysCard) {
        startPage();
//        if (!permissionService.hasAnyRoles("sadmin,admin")) {
        SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
        sysCard.setAgentId(agent.getAgentId());
//        }
        List<SysCard> list = sysCardService.selectSysCardList(sysCard);
        return getDataTable(list);
    }

    /**
     * 导出卡密列表
     */
    @PreAuthorize("@ss.hasPermi('agent:agentCard:export')")
    @AgentPermCheck
    @Log(title = "卡密", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysCard sysCard) {
//        if (!permissionService.hasAnyRoles("sadmin,admin")) {
        SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
        sysCard.setAgentId(agent.getAgentId());
//        }
        List<SysCard> list = sysCardService.selectSysCardList(sysCard);
        ExcelUtil<SysCard> util = new ExcelUtil<SysCard>(SysCard.class);
        return util.exportExcel(list, "卡密数据");
    }

    /**
     * 获取卡密详细信息
     */
    @PreAuthorize("@ss.hasPermi('agent:agentCard:query')")
    @AgentPermCheck
    @GetMapping(value = "/{cardId}")
    public AjaxResult getInfo(@PathVariable("cardId") Long cardId) {
        return AjaxResult.success(sysCardService.selectSysCardByCardId(cardId));
    }

    /**
     * 新增卡密
     */
    @PreAuthorize("@ss.hasPermi('agent:agentCard:add')")
    @AgentPermCheck
    @Log(title = "卡密", businessType = BusinessType.INSERT)
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult add(@RequestBody SysCard sysCard) {
        if (sysCard.getTemplateId() == null) {
            return AjaxResult.error("未指定卡类，批量制卡失败");
        }
        if (sysCard.getGenQuantity() == null || sysCard.getGenQuantity() < 0) {
            return AjaxResult.error("制卡数量有误，批量制卡失败");
        }
        SysCardTemplate sysCardTemplate = sysCardTemplateService.selectSysCardTemplateByTemplateId(sysCard.getTemplateId());
        if (sysCardTemplate == null) {
            return AjaxResult.error("卡类不存在，批量制卡失败");
        }
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
            // 判断是否有代理该卡的权限
            SysAgentItem agentItem = sysAgentItemService.checkAgentItem(null, agent.getAgentId(), TemplateType.CHARGE_CARD, sysCard.getTemplateId());
            // 计算金额
            BigDecimal unitPrice = agentItem.getAgentPrice();
            // 获取代理价格
            if (unitPrice == null) {
                throw new ServiceException("商品未设定价格");
            }
            BigDecimal totalFee = unitPrice.multiply(BigDecimal.valueOf(sysCard.getGenQuantity()));
            synchronized (SysAgentCardController.class) {
                // 扣除余额
                BalanceChangeVo change = new BalanceChangeVo();
                change.setUserId(getUserId());
                change.setUpdateBy(getUsername());
                change.setType(BalanceChangeType.CONSUME);
                change.setDescription("批量制卡：[" + sysCardTemplate.getApp().getAppName() + "]" + sysCardTemplate.getCardName() + "，" + sysCard.getGenQuantity() + "张，单价" + unitPrice);
                change.setAvailablePayBalance(totalFee.negate());
                // 扣款
                userService.updateUserBalance(change);
            }
        }
        List<SysCard> sysCardList = sysCardTemplateService.genSysCardBatch(sysCardTemplate, sysCard.getGenQuantity(), UserConstants.NO, UserConstants.YES, sysCard.getRemark());
        List<Map<String, String>> resultList = new ArrayList<>();
        for (SysCard item : sysCardList) {
            Map<String, String> map = new HashMap<>();
            map.put("cardNo", item.getCardNo());
            map.put("cardPass", item.getCardPass());
            map.put("expireTime", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS, item.getExpireTime()));
            resultList.add(map);
        }
        return AjaxResult.success("生成完毕", resultList).put("cardName", sysCardTemplate.getCardName());
    }

    /**
     * 修改卡密
     */
    @PreAuthorize("@ss.hasPermi('agent:agentCard:edit')")
    @AgentPermCheck
    @Log(title = "卡密", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysCard sysCard) {
        sysCard.setUpdateBy(getUsername());
        return toAjax(sysCardService.updateSysCard(sysCard));
    }

    /**
     * 删除卡密
     */
    @PreAuthorize("@ss.hasPermi('agent:agentCard:remove')")
    @AgentPermCheck
    @Log(title = "卡密", businessType = BusinessType.DELETE)
    @DeleteMapping("/{cardIds}")
    public AjaxResult remove(@PathVariable Long[] cardIds) {
        return toAjax(sysCardService.deleteSysCardByCardIds(cardIds));
    }

    /**
     * 查询卡密模板列表
     */
    @PreAuthorize("@ss.hasAnyRoles('agent,admin,sadmin')")
    @AgentPermCheck
    @GetMapping("/cardTemplate/listAll")
    public TableDataInfo list(SysCardTemplate sysCardTemplate) {
        List<SysCardTemplate> list = new ArrayList<>();
        if (sysCardTemplate.getAppId() != null) {
            if (!permissionService.hasAnyRoles("sadmin,admin")) {
                Set<Long> appIds = new HashSet<>();
                // 获取我的代理ID
                SysAgent agent = sysAgentService.selectSysAgentByUserId(getLoginUser().getUserId());
                if (agent != null) {
                    // 获取代理的所有卡类
                    List<SysAgentItem> agentItems = sysAgentItemService.selectSysAgentItemByAgentId(agent.getAgentId());
                    // 获取所有卡类信息
                    for (SysAgentItem item : agentItems) {
                        if (item.getTemplateType() == TemplateType.CHARGE_CARD) {
                            SysCardTemplate template = sysCardTemplateService.selectSysCardTemplateByTemplateId(item.getTemplateId());
                            if (sysCardTemplate.getAppId() != null && !Objects.equals(template.getAppId(), sysCardTemplate.getAppId())) {
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
                List<SysCardTemplate> templates = sysCardTemplateService.selectSysCardTemplateList(sysCardTemplate);
                for (SysCardTemplate template : templates) {
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
    @PreAuthorize("@ss.hasPermi('agent:agentCard:list')")
    @AgentPermCheck
    @GetMapping("/selectBatchNoList")
    public AjaxResult selectBatchNoList() {
        Long agentId = null;
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            agentId = getUserId();
        }
        List<BatchNoVo> batchNoList = sysCardService.selectBatchNoList(agentId);
        return AjaxResult.success(batchNoList);
    }
}
