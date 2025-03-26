package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreRoleMenu;

import java.util.List;

/**
 * 档口子角色菜单Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreRoleMenuService {
    /**
     * 查询档口子角色菜单
     *
     * @param storeRoleMenuId 档口子角色菜单主键
     * @return 档口子角色菜单
     */
    public StoreRoleMenu selectStoreRoleMenuByStoreRoleMenuId(Long storeRoleMenuId);

    /**
     * 查询档口子角色菜单列表
     *
     * @param storeRoleMenu 档口子角色菜单
     * @return 档口子角色菜单集合
     */
    public List<StoreRoleMenu> selectStoreRoleMenuList(StoreRoleMenu storeRoleMenu);

    /**
     * 新增档口子角色菜单
     *
     * @param storeRoleMenu 档口子角色菜单
     * @return 结果
     */
    public int insertStoreRoleMenu(StoreRoleMenu storeRoleMenu);

    /**
     * 修改档口子角色菜单
     *
     * @param storeRoleMenu 档口子角色菜单
     * @return 结果
     */
    public int updateStoreRoleMenu(StoreRoleMenu storeRoleMenu);

    /**
     * 批量删除档口子角色菜单
     *
     * @param storeRoleMenuIds 需要删除的档口子角色菜单主键集合
     * @return 结果
     */
    public int deleteStoreRoleMenuByStoreRoleMenuIds(Long[] storeRoleMenuIds);

    /**
     * 删除档口子角色菜单信息
     *
     * @param storeRoleMenuId 档口子角色菜单主键
     * @return 结果
     */
    public int deleteStoreRoleMenuByStoreRoleMenuId(Long storeRoleMenuId);
}
