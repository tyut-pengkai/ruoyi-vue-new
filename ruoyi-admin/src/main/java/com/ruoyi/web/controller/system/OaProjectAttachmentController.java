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
import com.ruoyi.system.domain.OaProjectAttachment;
import com.ruoyi.system.service.IOaProjectAttachmentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目附件Controller
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@RestController
@RequestMapping("/system/attachment")
public class OaProjectAttachmentController extends BaseController
{
    @Autowired
    private IOaProjectAttachmentService oaProjectAttachmentService;

    /**
     * 查询项目附件列表
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaProjectAttachment oaProjectAttachment)
    {
        startPage();
        List<OaProjectAttachment> list = oaProjectAttachmentService.selectOaProjectAttachmentList(oaProjectAttachment);
        return getDataTable(list);
    }

    /**
     * 导出项目附件列表
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:export')")
    @Log(title = "项目附件", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OaProjectAttachment oaProjectAttachment)
    {
        List<OaProjectAttachment> list = oaProjectAttachmentService.selectOaProjectAttachmentList(oaProjectAttachment);
        ExcelUtil<OaProjectAttachment> util = new ExcelUtil<OaProjectAttachment>(OaProjectAttachment.class);
        util.exportExcel(response, list, "项目附件数据");
    }

    /**
     * 获取项目附件详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(oaProjectAttachmentService.selectOaProjectAttachmentById(id));
    }

    /**
     * 新增项目附件
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:add')")
    @Log(title = "项目附件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaProjectAttachment oaProjectAttachment)
    {
        return toAjax(oaProjectAttachmentService.insertOaProjectAttachment(oaProjectAttachment));
    }

    /**
     * 修改项目附件
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:edit')")
    @Log(title = "项目附件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaProjectAttachment oaProjectAttachment)
    {
        return toAjax(oaProjectAttachmentService.updateOaProjectAttachment(oaProjectAttachment));
    }

    /**
     * 删除项目附件
     */
    @PreAuthorize("@ss.hasPermi('system:attachment:remove')")
    @Log(title = "项目附件", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaProjectAttachmentService.deleteOaProjectAttachmentByIds(ids));
    }
}
