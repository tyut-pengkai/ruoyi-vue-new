package com.ruoyi.xkt.mapper;

import com.ruoyi.xkt.domain.StoreRoleAccount;

import java.util.List;

/**
 * 档口子角色账号Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreRoleAccountMapper {
    /**
     * 查询档口子角色账号
     *
     * @param storeRoleAccId 档口子角色账号主键
     * @return 档口子角色账号
     */
    public StoreRoleAccount selectStoreRoleAccountByStoreRoleAccId(Long storeRoleAccId);

    /**
     * 查询档口子角色账号列表
     *
     * @param storeRoleAccount 档口子角色账号
     * @return 档口子角色账号集合
     */
    public List<StoreRoleAccount> selectStoreRoleAccountList(StoreRoleAccount storeRoleAccount);

    /**
     * 新增档口子角色账号
     *
     * @param storeRoleAccount 档口子角色账号
     * @return 结果
     */
    public int insertStoreRoleAccount(StoreRoleAccount storeRoleAccount);

    /**
     * 修改档口子角色账号
     *
     * @param storeRoleAccount 档口子角色账号
     * @return 结果
     */
    public int updateStoreRoleAccount(StoreRoleAccount storeRoleAccount);

    /**
     * 删除档口子角色账号
     *
     * @param storeRoleAccId 档口子角色账号主键
     * @return 结果
     */
    public int deleteStoreRoleAccountByStoreRoleAccId(Long storeRoleAccId);

    /**
     * 批量删除档口子角色账号
     *
     * @param storeRoleAccIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreRoleAccountByStoreRoleAccIds(Long[] storeRoleAccIds);
}
