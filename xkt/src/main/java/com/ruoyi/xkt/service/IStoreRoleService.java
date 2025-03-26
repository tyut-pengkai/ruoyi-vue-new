package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreRole;

import java.util.List;

/**
 * 档口子角色Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreRoleService {
    /**
     * 查询档口子角色
     *
     * @param storeRoleId 档口子角色主键
     * @return 档口子角色
     */
    public StoreRole selectStoreRoleByStoreRoleId(Long storeRoleId);

    /**
     * 查询档口子角色列表
     *
     * @param storeRole 档口子角色
     * @return 档口子角色集合
     */
    public List<StoreRole> selectStoreRoleList(StoreRole storeRole);

    /**
     * 新增档口子角色
     *
     * @param storeRole 档口子角色
     * @return 结果
     */
    public int insertStoreRole(StoreRole storeRole);

    /**
     * 修改档口子角色
     *
     * @param storeRole 档口子角色
     * @return 结果
     */
    public int updateStoreRole(StoreRole storeRole);

    /**
     * 批量删除档口子角色
     *
     * @param storeRoleIds 需要删除的档口子角色主键集合
     * @return 结果
     */
    public int deleteStoreRoleByStoreRoleIds(Long[] storeRoleIds);

    /**
     * 删除档口子角色信息
     *
     * @param storeRoleId 档口子角色主键
     * @return 结果
     */
    public int deleteStoreRoleByStoreRoleId(Long storeRoleId);
}
