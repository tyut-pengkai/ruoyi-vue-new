package com.ruoyi.xkt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.xkt.domain.StoreRole;

import java.util.List;

/**
 * 档口子角色Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreRoleMapper extends BaseMapper<StoreRole> {
    /**
     * 查询档口子角色
     *
     * @param id 档口子角色主键
     * @return 档口子角色
     */
    public StoreRole selectStoreRoleByStoreRoleId(Long id);

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
     * 删除档口子角色
     *
     * @param id 档口子角色主键
     * @return 结果
     */
    public int deleteStoreRoleByStoreRoleId(Long id);

    /**
     * 批量删除档口子角色
     *
     * @param storeRoleIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreRoleByStoreRoleIds(Long[] storeRoleIds);
}
