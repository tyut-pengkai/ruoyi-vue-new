package com.ruoyi.bookSys.controller;

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
import com.ruoyi.bookSys.domain.Residents;
import com.ruoyi.bookSys.service.IResidentsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 住户信息Controller
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
@RestController
@RequestMapping("/bookSys/residents")
public class ResidentsController extends BaseController
{
    @Autowired
    private IResidentsService residentsService;

    /**
     * 查询住户信息列表
     */
    @PreAuthorize("@ss.hasPermi('bookSys:residents:list')")
    @GetMapping("/list")
    public TableDataInfo list(Residents residents)
    {
        startPage();
        List<Residents> list = residentsService.selectResidentsList(residents);
        return getDataTable(list);
    }

    /**
     * 导出住户信息列表
     */
    @PreAuthorize("@ss.hasPermi('bookSys:residents:export')")
    @Log(title = "住户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Residents residents)
    {
        List<Residents> list = residentsService.selectResidentsList(residents);
        ExcelUtil<Residents> util = new ExcelUtil<Residents>(Residents.class);
        util.exportExcel(response, list, "住户信息数据");
    }

    /**
     * 获取住户信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('bookSys:residents:query')")
    @GetMapping(value = "/{residentId}")
    public AjaxResult getInfo(@PathVariable("residentId") Long residentId)
    {
        return success(residentsService.selectResidentsByResidentId(residentId));
    }

    /**
     * 新增住户信息
     */
    @PreAuthorize("@ss.hasPermi('bookSys:residents:add')")
    @Log(title = "住户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Residents residents)
    {
        return toAjax(residentsService.insertResidents(residents));
    }

    /**
     * 修改住户信息
     */
    @PreAuthorize("@ss.hasPermi('bookSys:residents:edit')")
    @Log(title = "住户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Residents residents)
    {
        return toAjax(residentsService.updateResidents(residents));
    }

    /**
     * 删除住户信息
     */
    @PreAuthorize("@ss.hasPermi('bookSys:residents:remove')")
    @Log(title = "住户信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{residentIds}")
    public AjaxResult remove(@PathVariable Long[] residentIds)
    {
        return toAjax(residentsService.deleteResidentsByResidentIds(residentIds));
    }
}
