package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreRole;
import com.ruoyi.xkt.service.IStoreRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口子角色Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/store-roles")
public class StoreRoleController extends BaseController {
    @Autowired
    private IStoreRoleService storeRoleService;

    /**
     * 查询档口子角色列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreRole storeRole) {
        startPage();
        List<StoreRole> list = storeRoleService.selectStoreRoleList(storeRole);
        return getDataTable(list);
    }

    /**
     * 导出档口子角色列表
     */
    @PreAuthorize("@ss.hasPermi('system:role:export')")
    @Log(title = "档口子角色", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreRole storeRole) {
        List<StoreRole> list = storeRoleService.selectStoreRoleList(storeRole);
        ExcelUtil<StoreRole> util = new ExcelUtil<StoreRole>(StoreRole.class);
        util.exportExcel(response, list, "档口子角色数据");
    }

    /**
     * 获取档口子角色详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{storeRoleId}")
    public AjaxResult getInfo(@PathVariable("storeRoleId") Long storeRoleId) {
        return success(storeRoleService.selectStoreRoleByStoreRoleId(storeRoleId));
    }

    /**
     * 新增档口子角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @Log(title = "档口子角色", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoreRole storeRole) {
        return toAjax(storeRoleService.insertStoreRole(storeRole));
    }

    /**
     * 修改档口子角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "档口子角色", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoreRole storeRole) {
        return toAjax(storeRoleService.updateStoreRole(storeRole));
    }

    /**
     * 删除档口子角色
     */
    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @Log(title = "档口子角色", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeRoleIds}")
    public AjaxResult remove(@PathVariable Long[] storeRoleIds) {
        return toAjax(storeRoleService.deleteStoreRoleByStoreRoleIds(storeRoleIds));
    }
}
