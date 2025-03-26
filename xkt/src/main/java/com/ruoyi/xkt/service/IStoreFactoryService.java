package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreFactory;

import java.util.List;

/**
 * 档口合作工厂Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreFactoryService {
    /**
     * 查询档口合作工厂
     *
     * @param storeFacId 档口合作工厂主键
     * @return 档口合作工厂
     */
    public StoreFactory selectStoreFactoryByStoreFacId(Long storeFacId);

    /**
     * 查询档口合作工厂列表
     *
     * @param storeFactory 档口合作工厂
     * @return 档口合作工厂集合
     */
    public List<StoreFactory> selectStoreFactoryList(StoreFactory storeFactory);

    /**
     * 新增档口合作工厂
     *
     * @param storeFactory 档口合作工厂
     * @return 结果
     */
    public int insertStoreFactory(StoreFactory storeFactory);

    /**
     * 修改档口合作工厂
     *
     * @param storeFactory 档口合作工厂
     * @return 结果
     */
    public int updateStoreFactory(StoreFactory storeFactory);

    /**
     * 批量删除档口合作工厂
     *
     * @param storeFacIds 需要删除的档口合作工厂主键集合
     * @return 结果
     */
    public int deleteStoreFactoryByStoreFacIds(Long[] storeFacIds);

    /**
     * 删除档口合作工厂信息
     *
     * @param storeFacId 档口合作工厂主键
     * @return 结果
     */
    public int deleteStoreFactoryByStoreFacId(Long storeFacId);
}
