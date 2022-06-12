package com.ruoyi.web.controller.agent;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.domain.SysAgentItem;
import com.ruoyi.agent.domain.vo.TemplateInfoVo;
import com.ruoyi.agent.service.ISysAgentItemService;
import com.ruoyi.agent.service.ISysAgentService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.TemplateType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.service.ISysCardTemplateService;
import com.ruoyi.system.service.ISysLoginCodeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 代理卡类关联Controller
 *
 * @author zwgu
 * @date 2022-06-11
 */
@RestController
@RequestMapping("/agent/agentItem")
public class SysAgentItemController extends BaseController {
    @Autowired
    private ISysAgentItemService sysAgentItemService;
    @Resource
    private ISysAgentService sysAgentService;
    @Resource
    private ISysCardTemplateService sysCardTemplateService;
    @Resource
    private ISysLoginCodeTemplateService sysLoginCodeTemplateService;
    @Resource
    private PermissionService permissionService;

    /**
     * 查询代理卡类关联列表
     */
    @PreAuthorize("@ss.hasPermi('agent:agentItem:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysAgentItem sysAgentItem) {
        startPage();
        List<SysAgentItem> list = sysAgentItemService.selectSysAgentItemList(sysAgentItem);
        for (SysAgentItem item : list) {
            fillTemplateInfo(item);
        }
        return getDataTable(list);
    }

    /**
     * 导出代理卡类关联列表
     */
    @PreAuthorize("@ss.hasPermi('agent:agentItem:export')")
    @Log(title = "代理卡类关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysAgentItem sysAgentItem) {
        List<SysAgentItem> list = sysAgentItemService.selectSysAgentItemList(sysAgentItem);
        ExcelUtil<SysAgentItem> util = new ExcelUtil<SysAgentItem>(SysAgentItem.class);
        util.exportExcel(response, list, "代理卡类关联数据");
    }

    /**
     * 获取代理卡类关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('agent:agentItem:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysAgentItemService.selectSysAgentItemById(id));
    }

    /**
     * 新增代理卡类关联
     */
    @PreAuthorize("@ss.hasPermi('agent:agentItem:add')")
    @Log(title = "代理卡类关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAgentItem sysAgentItem) {

        SysAgentItem s = new SysAgentItem();
        s.setAgentId(sysAgentItem.getAgentId());
        s.setTemplateType(sysAgentItem.getTemplateType());
        s.setTemplateId(sysAgentItem.getTemplateId());
        List<SysAgentItem> items = sysAgentItemService.selectSysAgentItemList(s);
        if (items.size() > 0) {
            throw new ServiceException("此代理已有代理该卡的权限，无法重复授权");
        }

        sysAgentItem.setCreateBy(getUsername());
        return toAjax(sysAgentItemService.insertSysAgentItem(sysAgentItem));
    }

    /**
     * 修改代理卡类关联
     */
    @PreAuthorize("@ss.hasPermi('agent:agentItem:edit')")
    @Log(title = "代理卡类关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAgentItem sysAgentItem) {

        SysAgentItem s = new SysAgentItem();
        s.setAgentId(sysAgentItem.getAgentId());
        s.setTemplateType(sysAgentItem.getTemplateType());
        s.setTemplateId(sysAgentItem.getTemplateId());
        List<SysAgentItem> items = sysAgentItemService.selectSysAgentItemList(s);
        if (items.size() > 0) {
            throw new ServiceException("此代理已有代理该卡的权限，无法重复授权");
        }

        sysAgentItem.setUpdateBy(getUsername());
        return toAjax(sysAgentItemService.updateSysAgentItem(sysAgentItem));
    }

    /**
     * 删除代理卡类关联
     */
    @PreAuthorize("@ss.hasPermi('agent:agentItem:remove')")
    @Log(title = "代理卡类关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysAgentItemService.deleteSysAgentItemByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('agent:agentItem:list')")
    @GetMapping("/grantableTemplate")
    public AjaxResult getGrantableTemplate() {
        List<TemplateInfoVo> templateList = new ArrayList<>();
        LoginUser loginUser = getLoginUser();
        if (permissionService.hasAnyRoles("sadmin,admin")) {
            List<SysCardTemplate> cardTemplateList = sysCardTemplateService.selectSysCardTemplateList(new SysCardTemplate());
            for (SysCardTemplate item : cardTemplateList) {
                TemplateInfoVo info = new TemplateInfoVo();
                info.setTemplateId(item.getTemplateId());
                info.setTemplateName(item.getCardName());
                info.setTemplateType(TemplateType.CHARGE_CARD);
                info.setAppName(item.getApp().getAppName());
                templateList.add(info);
            }
            List<SysLoginCodeTemplate> loginCodeTemplateList = sysLoginCodeTemplateService.selectSysLoginCodeTemplateList(new SysLoginCodeTemplate());
            for (SysLoginCodeTemplate item : loginCodeTemplateList) {
                TemplateInfoVo info = new TemplateInfoVo();
                info.setTemplateId(item.getTemplateId());
                info.setTemplateName(item.getCardName());
                info.setTemplateType(TemplateType.LOGIN_CODE);
                info.setAppName(item.getApp().getAppName());
                templateList.add(info);
            }
        } else {
            SysAgent sysAgent = sysAgentService.selectSysAgentByUserId(loginUser.getUserId());
            SysAgentItem sysAgentItem = new SysAgentItem();
            sysAgentItem.setAgentId(sysAgent.getAgentId());
            List<SysAgentItem> sysAgentItems = sysAgentItemService.selectSysAgentItemList(sysAgentItem);
            for (SysAgentItem item : sysAgentItems) {
                fillTemplateInfo(item);
                templateList.add(item.getTemplateInfo());
            }
        }
        return AjaxResult.success(templateList);
    }

    private void fillTemplateInfo(SysAgentItem item) {
        Long templateId = item.getTemplateId();
        TemplateInfoVo info = new TemplateInfoVo();
        if (item.getTemplateType() == TemplateType.CHARGE_CARD) {
            SysCardTemplate sysCardTemplate = sysCardTemplateService.selectSysCardTemplateByTemplateId(templateId);
            info.setTemplateId(sysCardTemplate.getTemplateId());
            info.setTemplateName(sysCardTemplate.getCardName());
            info.setTemplateType(TemplateType.CHARGE_CARD);
            info.setAppName(sysCardTemplate.getApp().getAppName());
        } else if (item.getTemplateType() == TemplateType.LOGIN_CODE) {
            SysLoginCodeTemplate sysLoginCodeTemplate = sysLoginCodeTemplateService.selectSysLoginCodeTemplateByTemplateId(templateId);
            info.setTemplateId(sysLoginCodeTemplate.getTemplateId());
            info.setTemplateName(sysLoginCodeTemplate.getCardName());
            info.setTemplateType(TemplateType.LOGIN_CODE);
            info.setAppName(sysLoginCodeTemplate.getApp().getAppName());
        }
        item.setTemplateInfo(info);
    }

}
