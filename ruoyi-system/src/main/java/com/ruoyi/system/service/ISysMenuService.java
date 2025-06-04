package com.ruoyi.system.service;

import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.model.*;
import com.ruoyi.system.domain.vo.menu.SysMenuDTO;

import java.util.Collection;
import java.util.List;

/**
 * 菜单 业务层
 *
 * @author ruoyi
 */
public interface ISysMenuService {

    /**
     * 获取菜单信息
     *
     * @param menuId
     * @return
     */
    MenuInfo getMenuById(Long menuId);

    /**
     * 菜单列表
     *
     * @param query
     * @return
     */
    List<MenuListItem> listMenu(MenuQuery query);

    /**
     * 菜单树
     *
     * @param query
     * @return
     */
    List<MenuTreeNode> getMenuTree(MenuQuery query);

    /**
     * 菜单树
     *
     * @param menus
     * @param <T>
     * @return
     */
    <T extends SysMenu> List<MenuTreeNode> getMenuTree(Collection<T> menus);

    /**
     * 创建菜单
     *
     * @param menuEdit
     * @return
     */
    Long createMenu(MenuInfoEdit menuEdit);

    /**
     * 修改菜单
     *
     * @param menuEdit
     * @return
     */
    InfluenceScope updateMenu(MenuInfoEdit menuEdit);

    /**
     * 批量修改菜单状态
     *
     * @param menuIds
     * @param status
     * @return
     */
    InfluenceScope batchUpdateStatus(List<Long> menuIds, String status);

    /**
     * 批量删除菜单
     *
     * @param menuIds
     * @return
     */
    InfluenceScope batchDelete(List<Long> menuIds);

    /**
     * 根据角色获取菜单列表
     *
     * @param roleId   角色ID
     * @param menuType 菜单类型
     * @return List<SysMenuDTO>
     */
    public List<SysMenuDTO> selectMenuListByRoleIdAndMenuType(Long roleId, String menuType);

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean hasChildByMenuId(Long menuId);

    /**
     * 查询菜单是否存在角色
     *
     * @param menuId 菜单ID
     * @return 结果 true 存在 false 不存在
     */
    public boolean checkMenuExistRole(Long menuId);
}
