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
import com.ruoyi.mmclub.domain.MSpecialties;
import com.ruoyi.mmclub.service.IMSpecialtiesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 医生专业管理Controller
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
@RestController
@RequestMapping("/mmclub/specialties")
public class MSpecialtiesController extends BaseController
{
    @Autowired
    private IMSpecialtiesService mSpecialtiesService;

    /**
     * 查询医生专业管理列表
     */
    @PreAuthorize("@ss.hasPermi('mmclub:specialties:list')")
    @GetMapping("/list")
    public TableDataInfo list(MSpecialties mSpecialties)
    {
        startPage();
        List<MSpecialties> list = mSpecialtiesService.selectMSpecialtiesList(mSpecialties);
        return getDataTable(list);
    }

    /**
     * 导出医生专业管理列表
     */
    @PreAuthorize("@ss.hasPermi('mmclub:specialties:export')")
    @Log(title = "医生专业管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MSpecialties mSpecialties)
    {
        List<MSpecialties> list = mSpecialtiesService.selectMSpecialtiesList(mSpecialties);
        ExcelUtil<MSpecialties> util = new ExcelUtil<MSpecialties>(MSpecialties.class);
        util.exportExcel(response, list, "医生专业管理数据");
    }

    /**
     * 获取医生专业管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('mmclub:specialties:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(mSpecialtiesService.selectMSpecialtiesById(id));
    }

    /**
     * 新增医生专业管理
     */
    @PreAuthorize("@ss.hasPermi('mmclub:specialties:add')")
    @Log(title = "医生专业管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MSpecialties mSpecialties)
    {
        return toAjax(mSpecialtiesService.insertMSpecialties(mSpecialties));
    }

    /**
     * 修改医生专业管理
     */
    @PreAuthorize("@ss.hasPermi('mmclub:specialties:edit')")
    @Log(title = "医生专业管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MSpecialties mSpecialties)
    {
        return toAjax(mSpecialtiesService.updateMSpecialties(mSpecialties));
    }

    /**
     * 删除医生专业管理
     */
    @PreAuthorize("@ss.hasPermi('mmclub:specialties:remove')")
    @Log(title = "医生专业管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mSpecialtiesService.deleteMSpecialtiesByIds(ids));
    }
}
