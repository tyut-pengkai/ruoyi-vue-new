package com.ruoyi.xkt.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.xkt.domain.StoreRole;
import com.ruoyi.xkt.mapper.StoreRoleMapper;
import com.ruoyi.xkt.service.IStoreRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 档口子角色Service业务层处理
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@Service
public class StoreRoleServiceImpl implements IStoreRoleService {
    @Autowired
    private StoreRoleMapper storeRoleMapper;

    /**
     * 查询档口子角色
     *
     * @param storeRoleId 档口子角色主键
     * @return 档口子角色
     */
    @Override
    public StoreRole selectStoreRoleByStoreRoleId(Long storeRoleId) {
        return storeRoleMapper.selectStoreRoleByStoreRoleId(storeRoleId);
    }

    /**
     * 查询档口子角色列表
     *
     * @param storeRole 档口子角色
     * @return 档口子角色
     */
    @Override
    public List<StoreRole> selectStoreRoleList(StoreRole storeRole) {
        return storeRoleMapper.selectStoreRoleList(storeRole);
    }

    /**
     * 新增档口子角色
     *
     * @param storeRole 档口子角色
     * @return 结果
     */
    @Override
    @Transactional
    public int insertStoreRole(StoreRole storeRole) {
        storeRole.setCreateTime(DateUtils.getNowDate());
        return storeRoleMapper.insertStoreRole(storeRole);
    }

    /**
     * 修改档口子角色
     *
     * @param storeRole 档口子角色
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStoreRole(StoreRole storeRole) {
        storeRole.setUpdateTime(DateUtils.getNowDate());
        return storeRoleMapper.updateStoreRole(storeRole);
    }

    /**
     * 批量删除档口子角色
     *
     * @param storeRoleIds 需要删除的档口子角色主键
     * @return 结果
     */
    @Override
    public int deleteStoreRoleByStoreRoleIds(Long[] storeRoleIds) {
        return storeRoleMapper.deleteStoreRoleByStoreRoleIds(storeRoleIds);
    }

    /**
     * 删除档口子角色信息
     *
     * @param storeRoleId 档口子角色主键
     * @return 结果
     */
    @Override
    public int deleteStoreRoleByStoreRoleId(Long storeRoleId) {
        return storeRoleMapper.deleteStoreRoleByStoreRoleId(storeRoleId);
    }
}
