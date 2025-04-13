package com.ruoyi.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.vo.menu.*;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.dto.menu.MenuDTO;
import com.ruoyi.system.domain.dto.menu.MenuListDTO;
import com.ruoyi.system.service.ISysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单信息
 *
 * @author ruoyi
 */
@Api(tags = "菜单信息")
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends XktBaseController {

    final ISysMenuService menuService;

    /**
     * 新增菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @ApiOperation(value = "新增菜单", httpMethod = "POST", response = R.class)
    @Log(title = "新增菜单", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody MenuVo menuVo) {
        return R.ok(menuService.create(BeanUtil.toBean(menuVo, MenuDTO.class)));
    }

    /**
     * 修改菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @ApiOperation(value = "修改菜单", httpMethod = "PUT", response = R.class)
    @Log(title = "修改菜单", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> edit(@Validated @RequestBody MenuVo menuVo) {
        return R.ok(menuService.update(BeanUtil.toBean(menuVo, MenuDTO.class)));
    }

    /**
     * 根据菜单编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @ApiOperation(value = "根据菜单编号获取详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{menuId}")
    public R<MenuResVo> getInfo(@PathVariable Long menuId) {
        return R.ok(BeanUtil.toBean(menuService.getById(menuId), MenuResVo.class));
    }

    /**
     * 删除菜单
     */
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @ApiOperation(value = "删除菜单", httpMethod = "DELETE", response = R.class)
    @Log(title = "删除菜单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public R<Integer> remove(@PathVariable("menuId") Long menuId) {
        return R.ok(this.menuService.delete(menuId));
    }

    /**
     * 获取菜单列表
     */
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @ApiOperation(value = "获取菜单列表", httpMethod = "POST", response = R.class)
    @PostMapping("/list")
    public R<List<MenuResVo>> list(@Validated @RequestBody MenuListVo listVo) {
        return R.ok(BeanUtil.copyToList(menuService.list(BeanUtil.toBean(listVo, MenuListDTO.class)), MenuResVo.class));
    }


    /**
     * 获取菜单下拉树列表
     */
    @ApiOperation(value = "获取菜单下拉树列表", httpMethod = "POST", response = R.class)
    @PostMapping("/treeSelect")
    public R<List<TreeSelectVO>> treeSelect(@Validated @RequestBody MenuListVo listVo) {
        return R.ok(BeanUtil.copyToList(menuService.treeSelect(BeanUtil.toBean(listVo, MenuListDTO.class)), TreeSelectVO.class));
    }


    /**
     * 加载对应角色菜单列表树
     */
    @ApiOperation(value = "加载对应角色菜单列表树", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/roleMenuTreeSelect/{roleId}")
    public R<UserRoleTreeSelectVO> roleMenuTreeSelect(@PathVariable("roleId") Long roleId) {
        return R.ok(BeanUtil.toBean(menuService.roleMenuTreeSelect(roleId), UserRoleTreeSelectVO.class));
    }













    // ===============================================================================================
    // ===============================================================================================
    // ===============================================================================================





    /**
     * 获取菜单列表
     */
   /* @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @GetMapping("/list")
    public AjaxResult list(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return success(menus);
    }*/

    /**
     * 根据菜单编号获取详细信息
     *//*
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @GetMapping(value = "/{menuId}")
    public AjaxResult getInfo(@PathVariable Long menuId) {
        return success(menuService.selectMenuById(menuId));
    }*/

    /**
     * 获取菜单下拉树列表
     */
    /*@GetMapping("/treeselect")
    public AjaxResult treeselect(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return success(menuService.buildMenuTreeSelect(menus));
    }*/

    /**
     * 加载对应角色菜单列表树
     */
    /*@GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public AjaxResult roleMenuTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysMenu> menus = menuService.selectMenuList(getUserId());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeSelect(menus));
        return ajax;
    }*/

    /**
     * 新增菜单
     */
    /*@PreAuthorize("@ss.hasPermi('system:menu:add')")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysMenu menu) {
        if (!menuService.checkMenuNameUnique(menu)) {
            return error("新增菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
            return error("新增菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }
        menu.setCreateBy(getUsername());
        return toAjax(menuService.insertMenu(menu));
    }*/

    /**
     * 修改菜单
     */
    /*@PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysMenu menu) {
        if (!menuService.checkMenuNameUnique(menu)) {
            return error("修改菜单'" + menu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(menu.getIsFrame()) && !StringUtils.ishttp(menu.getPath())) {
            return error("修改菜单'" + menu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (menu.getMenuId().equals(menu.getParentId())) {
            return error("修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menu.setUpdateBy(getUsername());
        return toAjax(menuService.updateMenu(menu));
    }*/

    /**
     * 删除菜单
     */
    /*@PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public AjaxResult remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return warn("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return warn("菜单已分配,不允许删除");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }*/
}