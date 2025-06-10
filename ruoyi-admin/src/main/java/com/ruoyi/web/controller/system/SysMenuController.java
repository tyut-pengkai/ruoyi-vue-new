package com.ruoyi.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.model.*;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.web.controller.system.vo.*;
import com.ruoyi.web.controller.xkt.vo.IdsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 菜单信息
 *
 * @author ruoyi
 */
@Api(tags = "系统菜单")
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/v1/sys/menu")
public class SysMenuController extends XktBaseController {

    final ISysMenuService menuService;

    final TokenService tokenService;

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "菜单列表查询 - 管理员")
    @PostMapping("/list")
    public R<List<MenuListItemVO>> list(@Validated @RequestBody MenuQueryVO vo) {
        MenuQuery query = BeanUtil.toBean(vo, MenuQuery.class);
        List<MenuListItem> list = menuService.listMenu(query);
        return R.ok(BeanUtil.copyToList(list, MenuListItemVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @ApiOperation(value = "菜单树查询 - 管理员")
    @PostMapping("/tree")
    public R<List<MenuTreeNodeVO>> tree(@Validated @RequestBody MenuQueryVO vo) {
        MenuQuery query = BeanUtil.toBean(vo, MenuQuery.class);
        List<MenuTreeNode> tree = menuService.getMenuTree(query);
        return R.ok(BeanUtil.copyToList(tree, MenuTreeNodeVO.class));
    }

    @ApiOperation(value = "菜单详情")
    @GetMapping(value = "/{id}")
    public R<MenuInfoVO> getInfo(@PathVariable("id") Long id) {
        MenuInfo infoDTO = menuService.getMenuById(id);
        return R.ok(BeanUtil.toBean(infoDTO, MenuInfoVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @ApiOperation("创建菜单")
    @PostMapping("create")
    public R<Long> create(@Valid @RequestBody MenuInfoEditVO vo) {
        MenuInfoEdit dto = BeanUtil.toBean(vo, MenuInfoEdit.class);
        dto.setMenuId(null);
        Long menuId = menuService.createMenu(dto);
        return R.ok(menuId);
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @ApiOperation("修改菜单")
    @PostMapping("edit")
    public R<Long> edit(@Valid @RequestBody MenuInfoEditVO vo) {
        Assert.notNull(vo.getMenuId(), "菜单ID不能为空");
        MenuInfoEdit dto = BeanUtil.toBean(vo, MenuInfoEdit.class);
        InfluenceScope scope = menuService.updateMenu(dto);
        // 清除用户缓存（退出登录）
        tokenService.deleteCacheUser(scope.getUserIds());
        return R.ok(vo.getMenuId());
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @ApiOperation("删除菜单")
    @PostMapping("/remove")
    public R<Integer> remove(@Validated @RequestBody IdsVO vo) {
        InfluenceScope scope = menuService.batchDelete(vo.getIds());
        // 清除用户缓存（退出登录）
        tokenService.deleteCacheUser(scope.getUserIds());
        return R.ok(scope.getCount());
    }

    @PreAuthorize("@ss.hasAnyRoles('admin,general_admin')")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @ApiOperation("修改菜单状态")
    @PostMapping("/changeStatus")
    public R<Integer> changeStatus(@Validated @RequestBody BatchOptStatusVO vo) {
        InfluenceScope scope = menuService.batchUpdateStatus(vo.getIds(), vo.getStatus());
        if (!Constants.SYS_NORMAL_STATUS.equals(vo.getStatus())) {
            // 清除用户缓存（退出登录）
            tokenService.deleteCacheUser(scope.getUserIds());
        }
        return R.ok(scope.getCount());
    }
}