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
import com.ruoyi.mmclub.domain.MHospitals;
import com.ruoyi.mmclub.service.IMHospitalsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 医院管理Controller
 * 
 * @author Jerry.Majesty
 * @date 2025-07-07
 */
@RestController
@RequestMapping("/mmclub/hospitals")
public class MHospitalsController extends BaseController
{
    @Autowired
    private IMHospitalsService mHospitalsService;

    /**
     * 查询医院管理列表
     */
    @PreAuthorize("@ss.hasPermi('mmclub:hospitals:list')")
    @GetMapping("/list")
    public TableDataInfo list(MHospitals mHospitals)
    {
        startPage();
        List<MHospitals> list = mHospitalsService.selectMHospitalsList(mHospitals);
        return getDataTable(list);
    }

    /**
     * 导出医院管理列表
     */
    @PreAuthorize("@ss.hasPermi('mmclub:hospitals:export')")
    @Log(title = "医院管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MHospitals mHospitals)
    {
        List<MHospitals> list = mHospitalsService.selectMHospitalsList(mHospitals);
        ExcelUtil<MHospitals> util = new ExcelUtil<MHospitals>(MHospitals.class);
        util.exportExcel(response, list, "医院管理数据");
    }

    /**
     * 获取医院管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('mmclub:hospitals:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(mHospitalsService.selectMHospitalsById(id));
    }

    /**
     * 新增医院管理
     */
    @PreAuthorize("@ss.hasPermi('mmclub:hospitals:add')")
    @Log(title = "医院管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MHospitals mHospitals)
    {
        return toAjax(mHospitalsService.insertMHospitals(mHospitals));
    }

    /**
     * 修改医院管理
     */
    @PreAuthorize("@ss.hasPermi('mmclub:hospitals:edit')")
    @Log(title = "医院管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MHospitals mHospitals)
    {
        return toAjax(mHospitalsService.updateMHospitals(mHospitals));
    }

    /**
     * 删除医院管理
     */
    @PreAuthorize("@ss.hasPermi('mmclub:hospitals:remove')")
    @Log(title = "医院管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(mHospitalsService.deleteMHospitalsByIds(ids));
    }
}
