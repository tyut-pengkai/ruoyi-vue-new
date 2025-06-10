/*
package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreRoleMenu;
import com.ruoyi.xkt.service.IStoreRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

*/
/**
 * 档口子角色菜单Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 *//*

@RestController
@RequestMapping("/rest/v1/store-role-menus")
public class StoreRoleMenuController extends XktBaseController {
    @Autowired
    private IStoreRoleMenuService storeRoleMenuService;

    */
/**
     * 查询档口子角色菜单列表
     *//*

//    // @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreRoleMenu storeRoleMenu) {
        startPage();
        List<StoreRoleMenu> list = storeRoleMenuService.selectStoreRoleMenuList(storeRoleMenu);
        return getDataTable(list);
    }

    */
/**
     * 导出档口子角色菜单列表
     *//*

//    // @PreAuthorize("@ss.hasPermi('system:menu:export')")
    @Log(title = "档口子角色菜单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreRoleMenu storeRoleMenu) {
        List<StoreRoleMenu> list = storeRoleMenuService.selectStoreRoleMenuList(storeRoleMenu);
        ExcelUtil<StoreRoleMenu> util = new ExcelUtil<StoreRoleMenu>(StoreRoleMenu.class);
        util.exportExcel(response, list, "档口子角色菜单数据");
    }

    */
/**
     * 获取档口子角色菜单详细信息
     *//*

//    // @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{storeRoleMenuId}")
    public R getInfo(@PathVariable("storeRoleMenuId") Long storeRoleMenuId) {
        return success(storeRoleMenuService.selectStoreRoleMenuByStoreRoleMenuId(storeRoleMenuId));
    }

    */
/**
     * 新增档口子角色菜单
     *//*

//    // @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @Log(title = "档口子角色菜单", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreRoleMenu storeRoleMenu) {
        return success(storeRoleMenuService.insertStoreRoleMenu(storeRoleMenu));
    }

    */
/**
     * 修改档口子角色菜单
     *//*

//    // @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @Log(title = "档口子角色菜单", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreRoleMenu storeRoleMenu) {
        return success(storeRoleMenuService.updateStoreRoleMenu(storeRoleMenu));
    }

    */
/**
     * 删除档口子角色菜单
     *//*

//    // @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @Log(title = "档口子角色菜单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeRoleMenuIds}")
    public R remove(@PathVariable Long[] storeRoleMenuIds) {
        return success(storeRoleMenuService.deleteStoreRoleMenuByStoreRoleMenuIds(storeRoleMenuIds));
    }
}
*/
