package com.ruoyi.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.model.*;
import com.ruoyi.common.core.page.PageVO;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.controller.system.vo.*;
import com.ruoyi.web.controller.xkt.vo.IdsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息
 *
 * @author ruoyi
 */
@Api(tags = "系统角色")
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/v1/sys/role")
public class SysRoleController extends XktBaseController {

    final ISysRoleService roleService;
    final TokenService tokenService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "角色分页查询 - 管理员")
    @PostMapping("/page")
    public R<PageVO<RoleListItemVO>> page(@Validated @RequestBody RoleQueryVO vo) {
        RoleQuery query = BeanUtil.toBean(vo, RoleQuery.class);
        Page<UserListItem> page = PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        roleService.listRole(query);
        return R.ok(PageVO.of(page, RoleListItemVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "所有角色 - 管理员")
    @PostMapping("/all")
    public R<List<RoleListItemVO>> all() {
        List<RoleListItem> all = roleService.listAllRole();
        return R.ok(BeanUtil.copyToList(all, RoleListItemVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin,store')")
    @ApiOperation(value = "角色详情 - 管理员/档口")
    @GetMapping(value = "/{id}")
    public R<RoleInfoVO> getInfo(@PathVariable("id") Long id) {
        RoleInfo infoDTO = roleService.getRoleById(id);
        RoleInfoVO vo = BeanUtil.toBean(infoDTO, RoleInfoVO.class);
        vo.setMenuIds(CollUtil.emptyIfNull(vo.getMenus()).stream().map(MenuInfoVO::getMenuId)
                .collect(Collectors.toList()));
        return R.ok(vo);
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @ApiOperation("创建角色 - 管理员")
    @PostMapping("create")
    public R<Long> create(@Valid @RequestBody RoleInfoEditVO vo) {
        RoleInfoEdit dto = BeanUtil.toBean(vo, RoleInfoEdit.class);
        dto.setRoleId(null);
        Long roleId = roleService.createRole(dto);
        return R.ok(roleId);
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @ApiOperation("修改角色 - 管理员")
    @PostMapping("edit")
    public R<Long> edit(@Valid @RequestBody RoleInfoEditVO vo) {
        Assert.notNull(vo.getRoleId(), "角色ID不能为空");
        RoleInfoEdit dto = BeanUtil.toBean(vo, RoleInfoEdit.class);
        InfluenceScope scope = roleService.updateRole(dto);
        // 清除用户缓存（退出登录）
        tokenService.deleteCacheUser(scope.getUserIds());
        return R.ok(vo.getRoleId());
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @Log(title = "角色管理", businessType = BusinessType.EXPORT)
    @ApiOperation("导出 - 管理员")
    @PostMapping("/export")
    public void export(@Validated @RequestBody RoleQueryVO vo, HttpServletResponse response) {
        RoleQuery query = BeanUtil.toBean(vo, RoleQuery.class);
        List<RoleListItem> list = roleService.listRole(query);
        ExcelUtil<RoleListItem> util = new ExcelUtil<>(RoleListItem.class);
        util.exportExcel(response, list, "角色数据");
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @ApiOperation("删除角色 - 管理员")
    @PostMapping("/remove")
    public R<Integer> remove(@Validated @RequestBody IdsVO vo) {
        InfluenceScope scope = roleService.batchDelete(vo.getIds());
        // 清除用户缓存（退出登录）
        tokenService.deleteCacheUser(scope.getUserIds());
        return R.ok(scope.getCount());
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @ApiOperation("修改角色状态 - 管理员")
    @PostMapping("/changeStatus")
    public R<Integer> changeStatus(@Validated @RequestBody BatchOptStatusVO vo) {
        InfluenceScope scope = roleService.batchUpdateStatus(vo.getIds(), vo.getStatus());
        if (!Constants.SYS_NORMAL_STATUS.equals(vo.getStatus())) {
            // 清除用户缓存（退出登录）
            tokenService.deleteCacheUser(scope.getUserIds());
        }
        return R.ok(scope.getCount());
    }

}
