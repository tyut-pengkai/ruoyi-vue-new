package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreRoleMenu;
import com.ruoyi.xkt.mapper.StoreRoleMenuMapper;
import com.ruoyi.xkt.service.IStoreRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口子角色菜单Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreRoleMenuServiceImpl implements IStoreRoleMenuService {
    @Autowired
    private StoreRoleMenuMapper storeRoleMenuMapper;

    /**
     * 查询档口子角色菜单
     *
     * @param storeRoleMenuId 档口子角色菜单主键
     * @return 档口子角色菜单
     */
    @Override
    public StoreRoleMenu selectStoreRoleMenuByStoreRoleMenuId(Long storeRoleMenuId) {
        return storeRoleMenuMapper.selectStoreRoleMenuByStoreRoleMenuId(storeRoleMenuId);
    }

    /**
     * 查询档口子角色菜单列表
     *
     * @param storeRoleMenu 档口子角色菜单
     * @return 档口子角色菜单
     */
    @Override
    public List<StoreRoleMenu> selectStoreRoleMenuList(StoreRoleMenu storeRoleMenu) {
        return storeRoleMenuMapper.selectStoreRoleMenuList(storeRoleMenu);
    }

    /**
     * 新增档口子角色菜单
     *
     * @param storeRoleMenu 档口子角色菜单
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreRoleMenu(StoreRoleMenu storeRoleMenu) {
        storeRoleMenu.setCreateTime(DateUtils.getNowDate());
        return storeRoleMenuMapper.insertStoreRoleMenu(storeRoleMenu);
    }

    /**
     * 修改档口子角色菜单
     *
     * @param storeRoleMenu 档口子角色菜单
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreRoleMenu(StoreRoleMenu storeRoleMenu) {
        storeRoleMenu.setUpdateTime(DateUtils.getNowDate());
        return storeRoleMenuMapper.updateStoreRoleMenu(storeRoleMenu);
    }

    /**
     * 批量删除档口子角色菜单
     *
     * @param storeRoleMenuIds 需要删除的档口子角色菜单主键
     * @return 结果
     */
    @Override
    public int deleteStoreRoleMenuByStoreRoleMenuIds(Long[] storeRoleMenuIds) {
        return storeRoleMenuMapper.deleteStoreRoleMenuByStoreRoleMenuIds(storeRoleMenuIds);
    }

    /**
     * 删除档口子角色菜单信息
     *
     * @param storeRoleMenuId 档口子角色菜单主键
     * @return 结果
     */
    @Override
    public int deleteStoreRoleMenuByStoreRoleMenuId(Long storeRoleMenuId) {
        return storeRoleMenuMapper.deleteStoreRoleMenuByStoreRoleMenuId(storeRoleMenuId);
    }
}
