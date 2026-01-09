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
import com.ruoyi.bookSys.domain.PropertyStaff;
import com.ruoyi.bookSys.service.IPropertyStaffService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 物业人员Controller
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
@RestController
@RequestMapping("/bookSys/staff")
public class PropertyStaffController extends BaseController
{
    @Autowired
    private IPropertyStaffService propertyStaffService;

    /**
     * 查询物业人员列表
     */
    @PreAuthorize("@ss.hasPermi('bookSys:staff:list')")
    @GetMapping("/list")
    public TableDataInfo list(PropertyStaff propertyStaff)
    {
        startPage();
        List<PropertyStaff> list = propertyStaffService.selectPropertyStaffList(propertyStaff);
        return getDataTable(list);
    }

    /**
     * 导出物业人员列表
     */
    @PreAuthorize("@ss.hasPermi('bookSys:staff:export')")
    @Log(title = "物业人员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PropertyStaff propertyStaff)
    {
        List<PropertyStaff> list = propertyStaffService.selectPropertyStaffList(propertyStaff);
        ExcelUtil<PropertyStaff> util = new ExcelUtil<PropertyStaff>(PropertyStaff.class);
        util.exportExcel(response, list, "物业人员数据");
    }

    /**
     * 获取物业人员详细信息
     */
    @PreAuthorize("@ss.hasPermi('bookSys:staff:query')")
    @GetMapping(value = "/{staffId}")
    public AjaxResult getInfo(@PathVariable("staffId") Long staffId)
    {
        return success(propertyStaffService.selectPropertyStaffByStaffId(staffId));
    }

    /**
     * 新增物业人员
     */
    @PreAuthorize("@ss.hasPermi('bookSys:staff:add')")
    @Log(title = "物业人员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PropertyStaff propertyStaff)
    {
        return toAjax(propertyStaffService.insertPropertyStaff(propertyStaff));
    }

    /**
     * 修改物业人员
     */
    @PreAuthorize("@ss.hasPermi('bookSys:staff:edit')")
    @Log(title = "物业人员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PropertyStaff propertyStaff)
    {
        return toAjax(propertyStaffService.updatePropertyStaff(propertyStaff));
    }

    /**
     * 删除物业人员
     */
    @PreAuthorize("@ss.hasPermi('bookSys:staff:remove')")
    @Log(title = "物业人员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{staffIds}")
    public AjaxResult remove(@PathVariable Long[] staffIds)
    {
        return toAjax(propertyStaffService.deletePropertyStaffByStaffIds(staffIds));
    }
}
