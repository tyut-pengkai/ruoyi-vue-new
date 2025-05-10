package com.ruoyi.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.HttpStatus;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.domain.vo.role.*;
import com.ruoyi.common.core.page.Page;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.SysPermissionService;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.domain.dto.role.*;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.controller.xkt.vo.storeProd.StoreProdPageVO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPageDTO;
import com.ruoyi.xkt.dto.storeProduct.StoreProdPageResDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 角色信息
 *
 * @author ruoyi
 */
@Api(tags = "角色信息")
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/v1/sys/role")
public class SysRoleController extends XktBaseController {

    final ISysRoleService roleService;
    final TokenService tokenService;
    final SysPermissionService permissionService;
    final ISysUserService userService;

    /**
     * 新增角色
     */
//    @PreAuthorize("@ss.hasPermi('system:role:add')")
    @ApiOperation(value = "新增角色", httpMethod = "POST", response = R.class)
    @Log(title = "新增角色", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Integer> create(@Validated @RequestBody RoleVO roleVO) {
        return R.ok(roleService.create(BeanUtil.toBean(roleVO, RoleDTO.class)));
    }

    /**
     * 修改角色
     */
//    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @ApiOperation(value = "修改角色", httpMethod = "PUT", response = R.class)
    @Log(title = "修改角色", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Integer> update(@Validated @RequestBody RoleUpdateVO updateVO) {
        Integer count = roleService.update(BeanUtil.toBean(updateVO, RoleUpdateDTO.class));
        if (count > 0) {
            // 更新缓存用户权限
            LoginUser loginUser = getLoginUser();
            if (ObjectUtils.isNotEmpty(loginUser.getUser()) && !loginUser.getUser().isAdmin()) {
                loginUser.setUser(userService.selectUserByUserName(loginUser.getUser().getUserName()));
                loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
                tokenService.setLoginUser(loginUser);
            }
            return R.ok();
        }
        throw new ServiceException("修改角色'" + updateVO.getRoleName() + "'失败，请联系管理员", HttpStatus.ERROR);
    }

    /**
     * 状态修改
     */
//    @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "状态修改", businessType = BusinessType.UPDATE)
    @ApiOperation(value = "状态修改", httpMethod = "PUT", response = R.class)
    @PutMapping("/change-status")
    public R<Integer> changeStatus(@Validated @RequestBody RoleUpdateStatusVO statusVO) {
        return R.ok(roleService.updateStatus(BeanUtil.toBean(statusVO, RoleUpdateStatusDTO.class)));
    }

    /**
     * 批量删除角色
     */
//    @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @ApiOperation(value = "批量删除角色", httpMethod = "DELETE", response = R.class)
    @Log(title = "批量删除角色", businessType = BusinessType.DELETE)
    @DeleteMapping("/batch")
    public R<Integer> batchRemove(@Validated @RequestBody RoleDeleteVO deleteVO) {
        return R.ok(roleService.batchRemove(BeanUtil.toBean(deleteVO, RoleDeleteDTO.class)));
    }

    /**
     * 根据角色编号获取详细信息
     */
//    @PreAuthorize("@ss.hasPermi('system:role:query')")
    @ApiOperation(value = "根据角色编号获取详细信息", httpMethod = "GET", response = R.class)
    @GetMapping(value = "/{roleId}")
    public R<RoleResVO> getInfo(@PathVariable Long roleId) {
        return R.ok(BeanUtil.toBean(roleService.getRoleInfo(roleId), RoleResVO.class));
    }

    /**
     * 查询角色列表
     */
//    @PreAuthorize("@ss.hasPermi('system:product:list')")
    @ApiOperation(value = "查询角色列表", httpMethod = "POST", response = R.class)
    @PostMapping("/page")
    public R<Page<RolePageResDTO>> page(@Validated @RequestBody RolePageVO pageVO) {
        return R.ok(roleService.page(BeanUtil.toBean(pageVO, RolePageDTO.class)));
    }


    // TODO 用户与角色之间的授权与取消授权功能，包括给系统开后门 帮用户调整角色
    // TODO 用户与角色之间的授权与取消授权功能，包括给系统开后门 帮用户调整角色
    // TODO 用户与角色之间的授权与取消授权功能，包括给系统开后门 帮用户调整角色
    // TODO 用户与角色之间的授权与取消授权功能，包括给系统开后门 帮用户调整角色
    // TODO 用户与角色之间的授权与取消授权功能，包括给系统开后门 帮用户调整角色














    // =======================================================================================================
    // =======================================================================================================
    // =======================================================================================================
    // =======================================================================================================








    /**
     * 根据角色编号获取详细信息
     */
    /*@PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping(value = "/{roleId}")
    public AjaxResult getInfo(@PathVariable Long roleId) {
        roleService.checkRoleDataScope(roleId);
        return success(roleService.selectRoleById(roleId));
    }*/

    /**
     * 新增角色
     */
    /*@PreAuthorize("@ss.hasPermi('system:role:add')")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysRole role) {
        if (!roleService.checkRoleNameUnique(role)) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return error("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setCreateBy(getUsername());
        return toAjax(roleService.insertRole(role));

    }*/

    /**
     * 修改保存角色
     */
    /*@PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        if (!roleService.checkRoleNameUnique(role)) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return error("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        role.setUpdateBy(getUsername());

        if (roleService.updateRole(role) > 0) {
            // 更新缓存用户权限
            LoginUser loginUser = getLoginUser();
            if (StringUtils.isNotNull(loginUser.getUser()) && !loginUser.getUser().isAdmin()) {
                loginUser.setUser(userService.selectUserByUserName(loginUser.getUser().getUserName()));
                loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
                tokenService.setLoginUser(loginUser);
            }
            return success();
        }
        return error("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }*/

    /**
     * 修改保存数据权限
     */
  /*  @PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    public AjaxResult dataScope(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        return toAjax(roleService.authDataScope(role));
    }*/

    /**
     * 状态修改
     */
    /*@PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        roleService.checkRoleDataScope(role.getRoleId());
        role.setUpdateBy(getUsername());
        return toAjax(roleService.updateRoleStatus(role));
    }*/

    /**
     * 删除角色
     */
   /* @PreAuthorize("@ss.hasPermi('system:role:remove')")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public AjaxResult remove(@PathVariable Long[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }*/

    /**
     * 获取角色选择框列表
     */
    /*@PreAuthorize("@ss.hasPermi('system:role:query')")
    @GetMapping("/optionselect")
    public AjaxResult optionselect() {
        return success(roleService.selectRoleAll());
    }*/

    /**
     * 查询已分配用户角色列表
     */
    /*@PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/allocatedList")
    public TableDataInfo allocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectAllocatedList(user);
        return getDataTable(list);
    }*/

    /**
     * 查询未分配用户角色列表
     */
   /* @PreAuthorize("@ss.hasPermi('system:role:list')")
    @GetMapping("/authUser/unallocatedList")
    public TableDataInfo unallocatedList(SysUser user) {
        startPage();
        List<SysUser> list = userService.selectUnallocatedList(user);
        return getDataTable(list);
    }*/

    /**
     * 取消授权用户
     */
    /*@PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    public AjaxResult cancelAuthUser(@RequestBody SysUserRole userRole) {
        return toAjax(roleService.deleteAuthUser(userRole));
    }*/

    /**
     * 批量取消授权用户
     */
    /*@PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    public AjaxResult cancelAuthUserAll(Long roleId, Long[] userIds) {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds));
    }*/

    /**
     * 批量选择用户授权
     */
    /*@PreAuthorize("@ss.hasPermi('system:role:edit')")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    public AjaxResult selectAuthUserAll(Long roleId, Long[] userIds) {
        roleService.checkRoleDataScope(roleId);
        return toAjax(roleService.insertAuthUsers(roleId, userIds));
    }*/

}
