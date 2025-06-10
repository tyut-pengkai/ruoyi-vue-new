package com.ruoyi.web.controller.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.domain.model.MenuInfo;
import com.ruoyi.common.core.domain.model.MenuListItem;
import com.ruoyi.common.core.domain.model.MenuQuery;
import com.ruoyi.common.core.domain.model.MenuTreeNode;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.web.controller.system.vo.MenuInfoVO;
import com.ruoyi.web.controller.system.vo.MenuListItemVO;
import com.ruoyi.web.controller.system.vo.MenuQueryVO;
import com.ruoyi.web.controller.system.vo.MenuTreeNodeVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 菜单信息
 *
 * @author ruoyi
 */
@Api(tags = "档口菜单")
@RequiredArgsConstructor
@RestController
@RequestMapping("/rest/v1/store/menu")
public class StoreMenuController extends XktBaseController {

    final ISysMenuService menuService;

    final TokenService tokenService;

    @PreAuthorize("@ss.hasAnyRoles('store')")
    @ApiOperation(value = "菜单列表查询 - 档口")
    @PostMapping("/list")
    public R<List<MenuListItemVO>> listByStore(@Validated @RequestBody MenuQueryVO vo) {
        MenuQuery query = BeanUtil.toBean(vo, MenuQuery.class);
        Set<Long> usableMenuIds = menuService.storeUsableMenuIds();
        List<MenuListItem> list = menuService.listMenu(query)
                .stream()
                .filter(o -> usableMenuIds.contains(o.getMenuId()))
                .collect(Collectors.toList());
        return R.ok(BeanUtil.copyToList(list, MenuListItemVO.class));
    }

    @PreAuthorize("@ss.hasAnyRoles('store')")
    @ApiOperation(value = "菜单树查询 - 档口")
    @PostMapping("/tree")
    public R<List<MenuTreeNodeVO>> treeByStore(@Validated @RequestBody MenuQueryVO vo) {
        MenuQuery query = BeanUtil.toBean(vo, MenuQuery.class);
        Set<Long> usableMenuIds = menuService.storeUsableMenuIds();
        if (CollUtil.isNotEmpty(query.getMenuIds())) {
            query.setMenuIds(new ArrayList<>(CollUtil.intersection(usableMenuIds, query.getMenuIds())));
        } else {
            query.setMenuIds(new ArrayList<>(usableMenuIds));
        }
        List<MenuTreeNode> tree = menuService.getMenuTree(query);
        return R.ok(BeanUtil.copyToList(tree, MenuTreeNodeVO.class));
    }

    @ApiOperation(value = "菜单详情")
    @GetMapping(value = "/{id}")
    public R<MenuInfoVO> getInfo(@PathVariable("id") Long id) {
        MenuInfo infoDTO = menuService.getMenuById(id);
        return R.ok(BeanUtil.toBean(infoDTO, MenuInfoVO.class));
    }
}