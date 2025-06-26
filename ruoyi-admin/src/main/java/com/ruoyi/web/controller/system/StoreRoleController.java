package com.ruoyi.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.model.*;
import com.ruoyi.common.core.page.PageVO;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.controller.system.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 角色信息
 *
 * @author ruoyi
 */
@Api(tags = "档口角色")
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/v1/store/role")
public class StoreRoleController extends XktBaseController {

    final ISysRoleService roleService;
    final TokenService tokenService;
    final ISysUserService userService;
    final ISysMenuService sysMenuService;


    @PreAuthorize("@ss.hasAnyRoles('store')")
    @ApiOperation(value = "角色分页查询 - 档口")
    @PostMapping("/page")
    public R<PageVO<RoleListItemVO>> pageByStore(@Validated @RequestBody RoleQueryVO vo) {
        RoleQuery query = BeanUtil.toBean(vo, RoleQuery.class);
        Long storeId = SecurityUtils.getStoreId();
        if (storeId == null) {
            return R.ok(PageVO.empty(vo));
        }
        // 只能查询当前档口
        query.setStoreIds(Collections.singletonList(storeId));
        Page<UserListItem> page = PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        roleService.listRole(query);
        return R.ok(PageVO.of(page, RoleListItemVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('store')")
    @ApiOperation(value = "所有角色 - 档口")
    @PostMapping("/all")
    public R<List<RoleListItemVO>> allByStore() {
        Long storeId = SecurityUtils.getStoreId();
        if (storeId == null) {
            return R.ok(ListUtil.empty());
        }
        RoleQuery query = new RoleQuery();
        // 只能查询当前档口
        query.setStoreIds(Collections.singletonList(SecurityUtils.getStoreId()));
        List<RoleListItem> all = roleService.listRole(query);
        return R.ok(BeanUtil.copyToList(all, RoleListItemVO.class));
    }


    @PreAuthorize("@ss.hasAnyRoles('store')")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @ApiOperation("创建角色 - 档口")
    @PostMapping("/create")
    public R<Long> createByStore(@Valid @RequestBody RoleInfoEditByStoreVO vo) {
        Assert.notNull(SecurityUtils.getStoreId());
        RoleInfoEdit dto = BeanUtil.toBean(vo, RoleInfoEdit.class);
        dto.setRoleId(null);
        dto.setStoreId(SecurityUtils.getStoreId());
        //档口的roleKey使用uuid
        dto.setRoleKey(IdUtil.fastSimpleUUID());
        Set<Long> usableMenuIds = sysMenuService.storeUsableMenuIds();
        CollUtil.emptyIfNull(dto.getMenuIds())
                .forEach(menuId -> Assert.isTrue(usableMenuIds.contains(menuId), "菜单不可用"));
        Long roleId = roleService.createRole(dto);
        return R.ok(roleId);
    }

    @PreAuthorize("@ss.hasAnyRoles('store')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @ApiOperation("修改角色 - 档口")
    @PostMapping("/edit")
    public R<Long> editByStore(@Valid @RequestBody RoleInfoEditByStoreVO vo) {
        Assert.notNull(SecurityUtils.getStoreId());
        Assert.notNull(vo.getRoleId(), "角色ID不能为空");
        RoleInfo info = roleService.getRoleById(vo.getRoleId());
        Assert.isTrue(Objects.equals(info.getStoreId(), SecurityUtils.getStoreId()), "档口ID不匹配");
        RoleInfoEdit dto = BeanUtil.toBean(vo, RoleInfoEdit.class);
        //档口的roleKey不变
        dto.setRoleKey(info.getRoleKey());
        Set<Long> usableMenuIds = sysMenuService.storeUsableMenuIds();
        CollUtil.emptyIfNull(dto.getMenuIds())
                .forEach(menuId -> Assert.isTrue(usableMenuIds.contains(menuId), "菜单不可用"));
        InfluenceScope scope = roleService.updateRole(dto);
        // 清除用户缓存（退出登录）
        tokenService.deleteCacheUser(scope.getUserIds());
        return R.ok(vo.getRoleId());
    }

    @PreAuthorize("@ss.hasAnyRoles('store')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @ApiOperation("修改角色状态 - 档口")
    @PostMapping("/changeStatus")
    public R<Integer> changeStatusByStore(@Validated @RequestBody BatchOptStatusVO vo) {
        Long storeId = SecurityUtils.getStoreId();
        Assert.notNull(storeId);
        List<RoleListItem> roles = roleService.listRole(RoleQuery.builder()
                .storeIds(Collections.singletonList(storeId)).build());
        roles.forEach(r -> Assert.isTrue(Objects.equals(r.getStoreId(), storeId), "档口ID不匹配"));
        InfluenceScope scope = roleService.batchUpdateStatus(vo.getIds(), vo.getStatus());
        if (!Constants.SYS_NORMAL_STATUS.equals(vo.getStatus())) {
            // 清除用户缓存（退出登录）
            tokenService.deleteCacheUser(scope.getUserIds());
        }
        return R.ok(scope.getCount());
    }

//    @PreAuthorize("@ss.hasAnyRoles('store')")
//    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
//    @ApiOperation("授权角色 - 档口")
//    @PostMapping("/bindUser")
    public R<Integer> bindUser(@Validated @RequestBody UserRoleBindReqVO vo) {
        RoleInfo info = roleService.getRoleById(vo.getRoleId());
        Assert.isTrue(Objects.equals(info.getStoreId(), SecurityUtils.getStoreId()), "档口ID不匹配");
        int count = roleService.bindUser(vo.getRoleId(), vo.getUserIds());
        // 清除用户缓存（退出登录）
        tokenService.deleteCacheUser(vo.getUserIds());
        return R.ok(count);
    }

//    @PreAuthorize("@ss.hasAnyRoles('store')")
//    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
//    @ApiOperation("取消授权角色 - 档口")
//    @PostMapping("/unbindUser")
    public R<Integer> unbindUser(@Validated @RequestBody UserRoleBindReqVO vo) {
        RoleInfo info = roleService.getRoleById(vo.getRoleId());
        Assert.isTrue(Objects.equals(info.getStoreId(), SecurityUtils.getStoreId()), "档口ID不匹配");
        int count = roleService.unbindUser(vo.getRoleId(), vo.getUserIds());
        // 清除用户缓存（退出登录）
        tokenService.deleteCacheUser(vo.getUserIds());
        return R.ok(count);
    }

}
