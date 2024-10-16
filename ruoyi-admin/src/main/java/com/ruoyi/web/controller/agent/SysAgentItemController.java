package com.ruoyi.web.controller.agent;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.domain.SysAgentItem;
import com.ruoyi.agent.domain.vo.TemplateInfoVo;
import com.ruoyi.agent.service.ISysAgentItemService;
import com.ruoyi.agent.service.ISysAgentUserService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.TemplateType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.framework.agent.anno.AgentPermCheck;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.system.domain.SysCardTemplate;
import com.ruoyi.system.domain.SysLoginCodeTemplate;
import com.ruoyi.system.service.ISysCardTemplateService;
import com.ruoyi.system.service.ISysLoginCodeTemplateService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 代理授权Controller
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
    private ISysAgentUserService sysAgentService;
    @Resource
    private ISysCardTemplateService sysCardTemplateService;
    @Resource
    private ISysLoginCodeTemplateService sysLoginCodeTemplateService;
    @Resource
    private PermissionService permissionService;

    /**
     * 查询代理授权列表
     */
    @PreAuthorize("@ss.hasPermi('agent:agentItem:list')")
    @AgentPermCheck
    @GetMapping("/list")
    public TableDataInfo list(SysAgentItem sysAgentItem) {
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            SysAgent sysAgent = sysAgentService.selectSysAgentByUserId(getUserId());
            sysAgentItem.getParams().put("_agentId", sysAgent.getAgentId());  // 用于查询自己的子代理
        }
        startPage();
        List<SysAgentItem> list = sysAgentItemService.selectSysAgentItemList(sysAgentItem);
        for (SysAgentItem item : list) {
            fillTemplateInfo(item);
        }
        return getDataTable(list);
    }

    /**
     * 导出代理授权列表
     */
    @PreAuthorize("@ss.hasPermi('agent:agentItem:export')")
    @AgentPermCheck
    @Log(title = "代理授权", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysAgentItem sysAgentItem) {
        List<SysAgentItem> list = sysAgentItemService.selectSysAgentItemList(sysAgentItem);
        ExcelUtil<SysAgentItem> util = new ExcelUtil<SysAgentItem>(SysAgentItem.class);
        util.exportExcel(response, list, "代理授权数据");
    }

    /**
     * 获取代理授权详细信息
     */
    @PreAuthorize("@ss.hasPermi('agent:agentItem:query')")
    @AgentPermCheck
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysAgentItemService.selectSysAgentItemById(id));
    }

    /**
     * 新增代理授权
     */
    @PreAuthorize("@ss.hasPermi('agent:agentItem:add')")
    @AgentPermCheck
    @Log(title = "代理授权", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAgentItem sysAgentItem) {
        if (sysAgentItem.getAgentId() == null || sysAgentItem.getAgentId() < 1) {
            throw new ServiceException("代理选择有误，不可选择根节点");
        }
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
            SysAgentItem item = sysAgentItemService.checkAgentItem(null, agent.getAgentId(), sysAgentItem.getTemplateType(), sysAgentItem.getTemplateId());
            if (sysAgentItem.getAgentPrice().compareTo(item.getAgentPrice()) < 0) {
                throw new ServiceException("代理价格设置有误，子代理价格不能低于您的代理价格");
            }
        }
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
     * 修改代理授权
     */
    @PreAuthorize("@ss.hasPermi('agent:agentItem:edit')")
    @AgentPermCheck
    @Log(title = "代理授权", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAgentItem sysAgentItem) {
        if (sysAgentItem.getAgentId() == null || sysAgentItem.getAgentId() < 1) {
            throw new ServiceException("代理选择有误，不可选择根节点");
        }
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            SysAgent agent = sysAgentService.selectSysAgentByUserId(getUserId());
            SysAgentItem item = sysAgentItemService.checkAgentItem(null, agent.getAgentId(), sysAgentItem.getTemplateType(), sysAgentItem.getTemplateId());
            if (sysAgentItem.getAgentPrice().compareTo(item.getAgentPrice()) < 0) {
                throw new ServiceException("代理价格设置有误，子代理价格不能低于您的代理价格");
            }
        }
        SysAgentItem s = new SysAgentItem();
        s.setAgentId(sysAgentItem.getAgentId());
        s.setTemplateType(sysAgentItem.getTemplateType());
        s.setTemplateId(sysAgentItem.getTemplateId());
        List<SysAgentItem> items = sysAgentItemService.selectSysAgentItemList(s);
        if (items.size() > 0) {
            for (SysAgentItem item : items) {
                if (Objects.equals(item.getId(), sysAgentItem.getId())) {
                    sysAgentItem.setUpdateBy(getUsername());
                    return toAjax(sysAgentItemService.updateSysAgentItem(sysAgentItem));
                }
            }
            throw new ServiceException("此代理已有代理该卡的权限，无法重复授权");
        } else {
            sysAgentItem.setUpdateBy(getUsername());
            return toAjax(sysAgentItemService.updateSysAgentItem(sysAgentItem));
        }
    }

    /**
     * 删除代理授权
     */
    @PreAuthorize("@ss.hasPermi('agent:agentItem:remove')")
    @AgentPermCheck
    @Log(title = "代理授权", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysAgentItemService.deleteSysAgentItemByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('agent:agentItem:list')")
    @AgentPermCheck
    @GetMapping("/grantableTemplate")
    public AjaxResult getGrantableTemplate(@RequestParam(required = false) Long agentId) {
        List<TemplateInfoVo> templateList = new ArrayList<>();
        LoginUser loginUser = getLoginUser();
        if (permissionService.hasAnyRoles("sadmin,admin")) { // 列出所有卡类
            List<SysCardTemplate> cardTemplateList = sysCardTemplateService.selectSysCardTemplateList(new SysCardTemplate());
            for (SysCardTemplate item : cardTemplateList) {
                TemplateInfoVo info = new TemplateInfoVo();
                info.setTemplateId(item.getTemplateId());
                info.setTemplateName(item.getCardName());
                info.setTemplateType(TemplateType.CHARGE_CARD);
                info.setAppName(item.getApp().getAppName());
                info.setPrice(item.getPrice());
                info.setMyPrice(BigDecimal.ZERO);
                templateList.add(info);
            }
            List<SysLoginCodeTemplate> loginCodeTemplateList = sysLoginCodeTemplateService.selectSysLoginCodeTemplateList(new SysLoginCodeTemplate());
            for (SysLoginCodeTemplate item : loginCodeTemplateList) {
                TemplateInfoVo info = new TemplateInfoVo();
                info.setTemplateId(item.getTemplateId());
                info.setTemplateName(item.getCardName());
                info.setTemplateType(TemplateType.LOGIN_CODE);
                info.setAppName(item.getApp().getAppName());
                info.setPrice(item.getPrice());
                info.setMyPrice(BigDecimal.ZERO);
                templateList.add(info);
            }
        } else { // 列出当前用户具有权限的卡类
            SysAgent sysAgent = sysAgentService.selectSysAgentByUserId(loginUser.getUserId());
            if(sysAgent == null) {
                throw new ServiceException("您无代理商权限");
            }
            SysAgentItem sysAgentItem = new SysAgentItem();
            sysAgentItem.setAgentId(sysAgent.getAgentId());
            List<SysAgentItem> sysAgentItems = sysAgentItemService.selectSysAgentItemList(sysAgentItem);
            for (SysAgentItem item : sysAgentItems) {
                fillTemplateInfo(item);
                templateList.add(item.getTemplateInfo());
            }
        }
        if(agentId != null) { // 获取当前所选的下级代理所具有的卡类
            // 获取已经具有授权的卡类
            SysAgentItem qSysAgentItem = new SysAgentItem();
            qSysAgentItem.setAgentId(agentId);
            List<SysAgentItem> list = sysAgentItemService.selectSysAgentItemList(qSysAgentItem);
            for (SysAgentItem item : list) {
                boolean flag = false; // 如果曾经具有授权，则应该加入到选择列表中供用户选择是否取消授权
                for (TemplateInfoVo templateInfoVo : templateList) {
                    if (Objects.equals(templateInfoVo.getTemplateId(), item.getTemplateId()) && templateInfoVo.getTemplateType() == item.getTemplateType()) {
                        templateInfoVo.setAgentItemId(item.getId());
                        templateInfoVo.setChecked(true);
                        templateInfoVo.setAgentPrice(item.getAgentPrice());
                        templateInfoVo.setExpireTime(item.getExpireTime());
                        templateInfoVo.setRemark(item.getRemark());
                        flag = true;
                        break;
                    }
                }
                if (!flag) { // 下级有此卡类当前用户却没有的
                    if(item.getTemplateInfo() == null) {
                        fillTemplateInfo(item);
                    }
                    TemplateInfoVo templateInfoVo = item.getTemplateInfo();
                    templateInfoVo.setAgentItemId(item.getId());
                    templateInfoVo.setChecked(true);
                    templateInfoVo.setAgentPrice(item.getAgentPrice());
                    templateInfoVo.setExpireTime(item.getExpireTime());
                    templateInfoVo.setRemark(item.getRemark());
                    if (permissionService.hasAnyRoles("sadmin,admin")) {
                        templateInfoVo.setMyPrice(BigDecimal.ZERO); // 曾经有代理权限，现在无代理权限
                    } else {
                        templateInfoVo.setMyPrice(null); // 曾经有代理权限，现在无代理权限
                    }
                    templateList.add(templateInfoVo);
                }
            }
        }
        return AjaxResult.success(templateList);
    }

    private void fillTemplateInfo(SysAgentItem item) {
        Long templateId = item.getTemplateId();
        TemplateInfoVo info = new TemplateInfoVo();
        if (item.getTemplateType() == TemplateType.CHARGE_CARD) {
            SysCardTemplate template = sysCardTemplateService.selectSysCardTemplateByTemplateId(templateId);
            info.setTemplateId(template.getTemplateId());
            info.setTemplateName(template.getCardName());
            info.setTemplateType(TemplateType.CHARGE_CARD);
            info.setAppName(template.getApp().getAppName());
            info.setPrice(template.getPrice());
            item.setPrice(template.getPrice());
            info.setMyPrice(item.getAgentPrice());
        } else if (item.getTemplateType() == TemplateType.LOGIN_CODE) {
            SysLoginCodeTemplate template = sysLoginCodeTemplateService.selectSysLoginCodeTemplateByTemplateId(templateId);
            info.setTemplateId(template.getTemplateId());
            info.setTemplateName(template.getCardName());
            info.setTemplateType(TemplateType.LOGIN_CODE);
            info.setAppName(template.getApp().getAppName());
            info.setPrice(template.getPrice());
            item.setPrice(template.getPrice());
            info.setMyPrice(item.getAgentPrice());
        }
        item.setTemplateInfo(info);
    }

}
