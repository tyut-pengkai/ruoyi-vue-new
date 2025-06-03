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
import com.ruoyi.system.domain.OaInvoiceApply;
import com.ruoyi.system.service.IOaInvoiceApplyService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 开票申请Controller
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@RestController
@RequestMapping("/system/apply")
public class OaInvoiceApplyController extends BaseController
{
    @Autowired
    private IOaInvoiceApplyService oaInvoiceApplyService;

    /**
     * 查询开票申请列表
     */
    @PreAuthorize("@ss.hasPermi('system:apply:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaInvoiceApply oaInvoiceApply)
    {
        startPage();
        List<OaInvoiceApply> list = oaInvoiceApplyService.selectOaInvoiceApplyList(oaInvoiceApply);
        return getDataTable(list);
    }

    /**
     * 导出开票申请列表
     */
    @PreAuthorize("@ss.hasPermi('system:apply:export')")
    @Log(title = "开票申请", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OaInvoiceApply oaInvoiceApply)
    {
        List<OaInvoiceApply> list = oaInvoiceApplyService.selectOaInvoiceApplyList(oaInvoiceApply);
        ExcelUtil<OaInvoiceApply> util = new ExcelUtil<OaInvoiceApply>(OaInvoiceApply.class);
        util.exportExcel(response, list, "开票申请数据");
    }

    /**
     * 获取开票申请详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:apply:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(oaInvoiceApplyService.selectOaInvoiceApplyById(id));
    }

    /**
     * 新增开票申请
     */
    @PreAuthorize("@ss.hasPermi('system:apply:add')")
    @Log(title = "开票申请", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaInvoiceApply oaInvoiceApply)
    {
        return toAjax(oaInvoiceApplyService.insertOaInvoiceApply(oaInvoiceApply));
    }

    /**
     * 修改开票申请
     */
    @PreAuthorize("@ss.hasPermi('system:apply:edit')")
    @Log(title = "开票申请", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaInvoiceApply oaInvoiceApply)
    {
        return toAjax(oaInvoiceApplyService.updateOaInvoiceApply(oaInvoiceApply));
    }

    /**
     * 删除开票申请
     */
    @PreAuthorize("@ss.hasPermi('system:apply:remove')")
    @Log(title = "开票申请", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaInvoiceApplyService.deleteOaInvoiceApplyByIds(ids));
    }
}
