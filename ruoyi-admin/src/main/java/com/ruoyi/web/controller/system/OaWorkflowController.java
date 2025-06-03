package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.OaWorkflow;
import com.ruoyi.system.service.IOaWorkflowService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 审批流程主Controller
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@RestController
@RequestMapping("/system/workflow")
public class OaWorkflowController extends BaseController
{
    @Autowired
    private IOaWorkflowService oaWorkflowService;

    /**
     * 查询审批流程主列表
     */
    @PreAuthorize("@ss.hasPermi('system:workflow:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaWorkflow oaWorkflow)
    {
        startPage();
        List<OaWorkflow> list = oaWorkflowService.selectOaWorkflowList(oaWorkflow);
        return getDataTable(list);
    }

    /**
     * 导出审批流程主列表
     */
    @PreAuthorize("@ss.hasPermi('system:workflow:export')")
    @Log(title = "审批流程主", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OaWorkflow oaWorkflow)
    {
        List<OaWorkflow> list = oaWorkflowService.selectOaWorkflowList(oaWorkflow);
        ExcelUtil<OaWorkflow> util = new ExcelUtil<OaWorkflow>(OaWorkflow.class);
        util.exportExcel(response, list, "审批流程主数据");
    }

    /**
     * 获取审批流程主详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:workflow:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(oaWorkflowService.selectOaWorkflowById(id));
    }

    /**
     * 新增审批流程主
     */
    @PreAuthorize("@ss.hasPermi('system:workflow:add')")
    @Log(title = "审批流程主", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaWorkflow oaWorkflow)
    {
        return toAjax(oaWorkflowService.insertOaWorkflow(oaWorkflow));
    }

    /**
     * 修改审批流程主
     */
    @PreAuthorize("@ss.hasPermi('system:workflow:edit')")
    @Log(title = "审批流程主", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaWorkflow oaWorkflow)
    {
        return toAjax(oaWorkflowService.updateOaWorkflow(oaWorkflow));
    }

    /**
     * 删除审批流程主
     */
    @PreAuthorize("@ss.hasPermi('system:workflow:remove')")
    @Log(title = "审批流程主", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaWorkflowService.deleteOaWorkflowByIds(ids));
    }
}
