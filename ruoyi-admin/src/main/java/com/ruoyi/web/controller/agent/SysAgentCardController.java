package com.ruoyi.web.controller.agent;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.domain.SysAgentItem;
import com.ruoyi.agent.service.ISysAgentItemService;
import com.ruoyi.agent.service.ISysAgentService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.TemplateType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.system.domain.SysCard;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.service.ISysCardService;
import com.ruoyi.system.service.ISysCardTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private ISysAgentService sysAgentService;
    @Resource
    private ISysCardTemplateService sysCardTemplateService;
    @Resource
    private PermissionService permissionService;

    /**
     * 查询卡密列表
     */
    @PreAuthorize("@ss.hasPermi('agent:agentCard:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysCard sysCard) {
        startPage();
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            sysCard.setCreateBy(getUsername());
        }
        List<SysCard> list = sysCardService.selectSysCardList(sysCard);
        return getDataTable(list);
    }

    /**
     * 导出卡密列表
     */
    @PreAuthorize("@ss.hasPermi('agent:agentCard:export')")
    @Log(title = "卡密", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysCard sysCard) {
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            sysCard.setCreateBy(getUsername());
        }
        List<SysCard> list = sysCardService.selectSysCardList(sysCard);
        ExcelUtil<SysCard> util = new ExcelUtil<SysCard>(SysCard.class);
        return util.exportExcel(list, "卡密数据");
    }

    /**
     * 获取卡密详细信息
     */
    @PreAuthorize("@ss.hasPermi('agent:agentCard:query')")
    @GetMapping(value = "/{cardId}")
    public AjaxResult getInfo(@PathVariable("cardId") Long cardId) {
        return AjaxResult.success(sysCardService.selectSysCardByCardId(cardId));
    }

    /**
     * 新增卡密
     */
    @PreAuthorize("@ss.hasPermi('agent:agentCard:add')")
    @Log(title = "卡密", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysCard sysCard) {
        if (sysCard.getTemplateId() == null) {
            sysCard.setCreateBy(getUsername());
            return toAjax(sysCardService.insertSysCard(sysCard));
        } else {
            if (sysCard.getGenQuantity() == null || sysCard.getGenQuantity() < 0) {
                return AjaxResult.error("制卡数量有误，批量制卡失败");
            }
            SysCardTemplate sysCardTemplate = sysCardTemplateService.selectSysCardTemplateByTemplateId(sysCard.getTemplateId());
            if (sysCardTemplate == null) {
                return AjaxResult.error("卡类不存在，批量制卡失败");
            }
            return toAjax(sysCardTemplateService.genSysCardBatch(sysCardTemplate, sysCard.getGenQuantity(), sysCard.getOnSale(), sysCard.getRemark()).size());
        }
    }

    /**
     * 修改卡密
     */
    @PreAuthorize("@ss.hasPermi('agent:agentCard:edit')")
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
    @Log(title = "卡密", businessType = BusinessType.DELETE)
    @DeleteMapping("/{cardIds}")
    public AjaxResult remove(@PathVariable Long[] cardIds) {
        return toAjax(sysCardService.deleteSysCardByCardIds(cardIds));
    }

    /**
     * 查询卡密模板列表
     */
    @PreAuthorize("@ss.hasRole('agent')")
    @GetMapping("/cardTemplate/listAll")
    public TableDataInfo list(SysCardTemplate sysCardTemplate) {
        List<SysCardTemplate> list = new ArrayList<>();
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            Set<Long> appIds = new HashSet<>();
            // 获取我的代理ID
            SysAgent agent = sysAgentService.selectSysAgentByUserId(getLoginUser().getUserId());
            // 获取代理的所有卡类
            List<SysAgentItem> agentItems = sysAgentItemService.selectSysAgentItemByAgentId(agent.getAgentId());
            // 获取所有卡类信息
            for (SysAgentItem item : agentItems) {
                if (item.getTemplateType() == TemplateType.CHARGE_CARD) {
                    SysCardTemplate template = sysCardTemplateService.selectSysCardTemplateByTemplateId(item.getTemplateId());
                    if (!appIds.contains(template.getTemplateId())) {
                        list.add(template);
                        appIds.add(template.getTemplateId());
                    }
                }
            }
        } else {
            list.addAll(sysCardTemplateService.selectSysCardTemplateList(sysCardTemplate));
        }
        return getDataTable(list);
    }
}
