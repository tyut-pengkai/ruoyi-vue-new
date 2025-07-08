package com.ruoyi.mmclub.controller;

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
import com.ruoyi.mmclub.domain.MDoctors;
import com.ruoyi.mmclub.service.IMDoctorsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 医生管理Controller
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
@RestController
@RequestMapping("/mmclub/doctors")
public class MDoctorsController extends BaseController
{
    @Autowired
    private IMDoctorsService mDoctorsService;

    /**
     * 查询医生管理列表
     */
    @PreAuthorize("@ss.hasPermi('mmclub:doctors:list')")
    @GetMapping("/list")
    public TableDataInfo list(MDoctors mDoctors)
    {
        startPage();
        List<MDoctors> list = mDoctorsService.selectMDoctorsList(mDoctors);
        return getDataTable(list);
    }

    /**
     * 导出医生管理列表
     */
    @PreAuthorize("@ss.hasPermi('mmclub:doctors:export')")
    @Log(title = "医生管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MDoctors mDoctors)
    {
        List<MDoctors> list = mDoctorsService.selectMDoctorsList(mDoctors);
        ExcelUtil<MDoctors> util = new ExcelUtil<MDoctors>(MDoctors.class);
        util.exportExcel(response, list, "医生管理数据");
    }

    /**
     * 获取医生管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('mmclub:doctors:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(mDoctorsService.selectMDoctorsById(id));
    }

    /**
     * 新增医生管理
     */
    @PreAuthorize("@ss.hasPermi('mmclub:doctors:add')")
    @Log(title = "医生管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MDoctors mDoctors)
    {
        return toAjax(mDoctorsService.insertMDoctors(mDoctors));
    }

    /**
     * 修改医生管理
     */
    @PreAuthorize("@ss.hasPermi('mmclub:doctors:edit')")
    @Log(title = "医生管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MDoctors mDoctors)
    {
        return toAjax(mDoctorsService.updateMDoctors(mDoctors));
    }

    /**
     * 删除医生管理
     */
    @PreAuthorize("@ss.hasPermi('mmclub:doctors:remove')")
    @Log(title = "医生管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mDoctorsService.deleteMDoctorsByIds(ids));
    }
}
