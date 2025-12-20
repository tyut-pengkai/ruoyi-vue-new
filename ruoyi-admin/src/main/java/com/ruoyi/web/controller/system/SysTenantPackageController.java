package com.ruoyi.web.controller.system;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysTenantPackage;
import com.ruoyi.system.service.ISysTenantPackageService;

/**
 * 租户套餐Controller
 *
 * @author ruoyi
 * @date 2025-12-19
 */
@RestController
@RequestMapping("/system/package")
public class SysTenantPackageController extends BaseController
{
    @Autowired
    private ISysTenantPackageService packageService;

    /**
     * 查询租户套餐列表
     */
    @PreAuthorize("@ss.hasPermi('system:package:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysTenantPackage sysTenantPackage)
    {
        startPage();
        List<SysTenantPackage> list = packageService.selectPackageList(sysTenantPackage);
        return getDataTable(list);
    }

    /**
     * 获取租户套餐详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:package:query')")
    @GetMapping(value = "/{packageId}")
    public AjaxResult getInfo(@PathVariable("packageId") Long packageId)
    {
        return success(packageService.selectPackageById(packageId));
    }

    /**
     * 新增租户套餐
     */
    @PreAuthorize("@ss.hasPermi('system:package:add')")
    @Log(title = "租户套餐", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysTenantPackage sysTenantPackage)
    {
        return toAjax(packageService.insertPackage(sysTenantPackage));
    }

    /**
     * 修改租户套餐
     */
    @PreAuthorize("@ss.hasPermi('system:package:edit')")
    @Log(title = "租户套餐", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysTenantPackage sysTenantPackage)
    {
        return toAjax(packageService.updatePackage(sysTenantPackage));
    }

    /**
     * 删除租户套餐
     */
    @PreAuthorize("@ss.hasPermi('system:package:remove')")
    @Log(title = "租户套餐", businessType = BusinessType.DELETE)
    @DeleteMapping("/{packageIds}")
    public AjaxResult remove(@PathVariable Long[] packageIds)
    {
        return toAjax(packageService.deletePackageByIds(packageIds));
    }

    /**
     * 查询套餐关联的菜单ID列表
     */
    @PreAuthorize("@ss.hasPermi('system:package:query')")
    @GetMapping(value = "/{packageId}/menus")
    public AjaxResult getPackageMenus(@PathVariable("packageId") Long packageId)
    {
        List<Long> menuIds = packageService.selectMenuIdsByPackageId(packageId);
        return success(menuIds);
    }
}
