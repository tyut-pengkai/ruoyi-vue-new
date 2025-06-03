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
import com.ruoyi.system.domain.OaProjectPayment;
import com.ruoyi.system.service.IOaProjectPaymentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 项目款项Controller
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@RestController
@RequestMapping("/system/payment")
public class OaProjectPaymentController extends BaseController
{
    @Autowired
    private IOaProjectPaymentService oaProjectPaymentService;

    /**
     * 查询项目款项列表
     */
    @PreAuthorize("@ss.hasPermi('system:payment:list')")
    @GetMapping("/list")
    public TableDataInfo list(OaProjectPayment oaProjectPayment)
    {
        startPage();
        List<OaProjectPayment> list = oaProjectPaymentService.selectOaProjectPaymentList(oaProjectPayment);
        return getDataTable(list);
    }

    /**
     * 导出项目款项列表
     */
    @PreAuthorize("@ss.hasPermi('system:payment:export')")
    @Log(title = "项目款项", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, OaProjectPayment oaProjectPayment)
    {
        List<OaProjectPayment> list = oaProjectPaymentService.selectOaProjectPaymentList(oaProjectPayment);
        ExcelUtil<OaProjectPayment> util = new ExcelUtil<OaProjectPayment>(OaProjectPayment.class);
        util.exportExcel(response, list, "项目款项数据");
    }

    /**
     * 获取项目款项详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:payment:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(oaProjectPaymentService.selectOaProjectPaymentById(id));
    }

    /**
     * 新增项目款项
     */
    @PreAuthorize("@ss.hasPermi('system:payment:add')")
    @Log(title = "项目款项", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody OaProjectPayment oaProjectPayment)
    {
        return toAjax(oaProjectPaymentService.insertOaProjectPayment(oaProjectPayment));
    }

    /**
     * 修改项目款项
     */
    @PreAuthorize("@ss.hasPermi('system:payment:edit')")
    @Log(title = "项目款项", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody OaProjectPayment oaProjectPayment)
    {
        return toAjax(oaProjectPaymentService.updateOaProjectPayment(oaProjectPayment));
    }

    /**
     * 删除项目款项
     */
    @PreAuthorize("@ss.hasPermi('system:payment:remove')")
    @Log(title = "项目款项", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(oaProjectPaymentService.deleteOaProjectPaymentByIds(ids));
    }
}
