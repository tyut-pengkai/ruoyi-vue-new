package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.domain.SysTenant;
import com.ruoyi.system.service.ISysTenantService;
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
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 租户信息Controller
 * 
 * @author W-yf
 * @date 2025-12-19
 */
@RestController
@RequestMapping("/link/tenant")
public class SysTenantController extends BaseController
{
    @Autowired
    private ISysTenantService sysTenantService;

    /**
     * 查询租户信息列表
     */
    @PreAuthorize("@ss.hasPermi('link:tenant:list')")
    @GetMapping("/list")
    public TableDataInfo list(
            SysTenant sysTenant)
    {
        startPage();
        List<SysTenant> list = sysTenantService.selectSysTenantList(sysTenant);
        return getDataTable(list);
    }

    /**
     * 导出租户信息列表
     */
    @PreAuthorize("@ss.hasPermi('link:tenant:export')")
    @Log(title = "租户信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysTenant sysTenant)
    {
        List<SysTenant> list = sysTenantService.selectSysTenantList(sysTenant);
        ExcelUtil<SysTenant> util = new ExcelUtil<SysTenant>(SysTenant.class);
        util.exportExcel(response, list, "租户信息数据");
    }

    /**
     * 获取租户信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('link:tenant:query')")
    @GetMapping(value = "/{tenantId}")
    public AjaxResult getInfo(@PathVariable("tenantId") Long tenantId)
    {
        return success(sysTenantService.selectSysTenantByTenantId(tenantId));
    }

    /**
     * 新增租户信息
     */
    @PreAuthorize("@ss.hasPermi('link:tenant:add')")
    @Log(title = "租户信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysTenant sysTenant)
    {
        return toAjax(sysTenantService.insertSysTenant(sysTenant));
    }

    /**
     * 修改租户信息
     */
    @PreAuthorize("@ss.hasPermi('link:tenant:edit')")
    @Log(title = "租户信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysTenant sysTenant)
    {
        return toAjax(sysTenantService.updateSysTenant(sysTenant));
    }

    /**
     * 删除租户信息
     */
    @PreAuthorize("@ss.hasPermi('link:tenant:remove')")
    @Log(title = "租户信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{tenantIds}")
    public AjaxResult remove(@PathVariable Long[] tenantIds)
    {
        return toAjax(sysTenantService.deleteSysTenantByTenantIds(tenantIds));
    }
}
