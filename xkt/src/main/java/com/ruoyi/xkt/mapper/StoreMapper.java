package com.ruoyi.xkt.mapper;

import com.ruoyi.xkt.domain.Store;

import java.util.List;

/**
 * 档口Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreMapper {
    /**
     * 查询档口
     *
     * @param storeId 档口主键
     * @return 档口
     */
    public Store selectStoreByStoreId(Long storeId);

    /**
     * 查询档口列表
     *
     * @param store 档口
     * @return 档口集合
     */
    public List<Store> selectStoreList(Store store);

    /**
     * 新增档口
     *
     * @param store 档口
     * @return 结果
     */
    public int insertStore(Store store);

    /**
     * 修改档口
     *
     * @param store 档口
     * @return 结果
     */
    public int updateStore(Store store);

    /**
     * 删除档口
     *
     * @param storeId 档口主键
     * @return 结果
     */
    public int deleteStoreByStoreId(Long storeId);

    /**
     * 批量删除档口
     *
     * @param storeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreByStoreIds(Long[] storeIds);
}
