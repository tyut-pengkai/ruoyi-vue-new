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
import com.ruoyi.system.domain.OaWorkflowNode;
import com.ruoyi.system.service.IOaWorkflowNodeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 流程审批节点Controller
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@RestController
@RequestMapping("/system/node")
public class OaWorkflowNodeController extends BaseController
{
    @Autowired
    private IOaWorkflowNodeService oaWorkflowNodeService;

    /**
     * 查询流程审批节点列表
     */
    @PreAuthorize("@ss.hasPermi('system:node:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaWorkflowNode oaWorkflowNode)
    {
        startPage();
        List<OaWorkflowNode> list = oaWorkflowNodeService.selectOaWorkflowNodeList(oaWorkflowNode);
        return getDataTable(list);
    }

    /**
     * 导出流程审批节点列表
     */
    @PreAuthorize("@ss.hasPermi('system:node:export')")
    @Log(title = "流程审批节点", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OaWorkflowNode oaWorkflowNode)
    {
        List<OaWorkflowNode> list = oaWorkflowNodeService.selectOaWorkflowNodeList(oaWorkflowNode);
        ExcelUtil<OaWorkflowNode> util = new ExcelUtil<OaWorkflowNode>(OaWorkflowNode.class);
        util.exportExcel(response, list, "流程审批节点数据");
    }

    /**
     * 获取流程审批节点详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:node:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(oaWorkflowNodeService.selectOaWorkflowNodeById(id));
    }

    /**
     * 新增流程审批节点
     */
    @PreAuthorize("@ss.hasPermi('system:node:add')")
    @Log(title = "流程审批节点", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaWorkflowNode oaWorkflowNode)
    {
        return toAjax(oaWorkflowNodeService.insertOaWorkflowNode(oaWorkflowNode));
    }

    /**
     * 修改流程审批节点
     */
    @PreAuthorize("@ss.hasPermi('system:node:edit')")
    @Log(title = "流程审批节点", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaWorkflowNode oaWorkflowNode)
    {
        return toAjax(oaWorkflowNodeService.updateOaWorkflowNode(oaWorkflowNode));
    }

    /**
     * 删除流程审批节点
     */
    @PreAuthorize("@ss.hasPermi('system:node:remove')")
    @Log(title = "流程审批节点", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaWorkflowNodeService.deleteOaWorkflowNodeByIds(ids));
    }
}
