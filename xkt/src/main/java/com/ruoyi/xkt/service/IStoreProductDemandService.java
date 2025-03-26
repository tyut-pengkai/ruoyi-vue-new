package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreProductDemand;

import java.util.List;

/**
 * 档口商品需求单Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductDemandService {
    /**
     * 查询档口商品需求单
     *
     * @param storeProdDemandId 档口商品需求单主键
     * @return 档口商品需求单
     */
    public StoreProductDemand selectStoreProductDemandByStoreProdDemandId(Long storeProdDemandId);

    /**
     * 查询档口商品需求单列表
     *
     * @param storeProductDemand 档口商品需求单
     * @return 档口商品需求单集合
     */
    public List<StoreProductDemand> selectStoreProductDemandList(StoreProductDemand storeProductDemand);

    /**
     * 新增档口商品需求单
     *
     * @param storeProductDemand 档口商品需求单
     * @return 结果
     */
    public int insertStoreProductDemand(StoreProductDemand storeProductDemand);

    /**
     * 修改档口商品需求单
     *
     * @param storeProductDemand 档口商品需求单
     * @return 结果
     */
    public int updateStoreProductDemand(StoreProductDemand storeProductDemand);

    /**
     * 批量删除档口商品需求单
     *
     * @param storeProdDemandIds 需要删除的档口商品需求单主键集合
     * @return 结果
     */
    public int deleteStoreProductDemandByStoreProdDemandIds(Long[] storeProdDemandIds);

    /**
     * 删除档口商品需求单信息
     *
     * @param storeProdDemandId 档口商品需求单主键
     * @return 结果
     */
    public int deleteStoreProductDemandByStoreProdDemandId(Long storeProdDemandId);
}
