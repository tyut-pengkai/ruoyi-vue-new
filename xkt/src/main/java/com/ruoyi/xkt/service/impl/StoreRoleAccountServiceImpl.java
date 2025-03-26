package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreRoleAccount;
import com.ruoyi.xkt.mapper.StoreRoleAccountMapper;
import com.ruoyi.xkt.service.IStoreRoleAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 档口子角色账号Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreRoleAccountServiceImpl implements IStoreRoleAccountService {
    @Autowired
    private StoreRoleAccountMapper storeRoleAccountMapper;

    /**
     * 查询档口子角色账号
     *
     * @param storeRoleAccId 档口子角色账号主键
     * @return 档口子角色账号
     */
    @Override
    public StoreRoleAccount selectStoreRoleAccountByStoreRoleAccId(Long storeRoleAccId) {
        return storeRoleAccountMapper.selectStoreRoleAccountByStoreRoleAccId(storeRoleAccId);
    }

    /**
     * 查询档口子角色账号列表
     *
     * @param storeRoleAccount 档口子角色账号
     * @return 档口子角色账号
     */
    @Override
    public List<StoreRoleAccount> selectStoreRoleAccountList(StoreRoleAccount storeRoleAccount) {
        return storeRoleAccountMapper.selectStoreRoleAccountList(storeRoleAccount);
    }

    /**
     * 新增档口子角色账号
     *
     * @param storeRoleAccount 档口子角色账号
     * @return 结果
     */
    @Override
    public int insertStoreRoleAccount(StoreRoleAccount storeRoleAccount) {
        storeRoleAccount.setCreateTime(DateUtils.getNowDate());
        return storeRoleAccountMapper.insertStoreRoleAccount(storeRoleAccount);
    }

    /**
     * 修改档口子角色账号
     *
     * @param storeRoleAccount 档口子角色账号
     * @return 结果
     */
    @Override
    public int updateStoreRoleAccount(StoreRoleAccount storeRoleAccount) {
        storeRoleAccount.setUpdateTime(DateUtils.getNowDate());
        return storeRoleAccountMapper.updateStoreRoleAccount(storeRoleAccount);
    }

    /**
     * 批量删除档口子角色账号
     *
     * @param storeRoleAccIds 需要删除的档口子角色账号主键
     * @return 结果
     */
    @Override
    public int deleteStoreRoleAccountByStoreRoleAccIds(Long[] storeRoleAccIds) {
        return storeRoleAccountMapper.deleteStoreRoleAccountByStoreRoleAccIds(storeRoleAccIds);
    }

    /**
     * 删除档口子角色账号信息
     *
     * @param storeRoleAccId 档口子角色账号主键
     * @return 结果
     */
    @Override
    public int deleteStoreRoleAccountByStoreRoleAccId(Long storeRoleAccId) {
        return storeRoleAccountMapper.deleteStoreRoleAccountByStoreRoleAccId(storeRoleAccId);
    }
}
