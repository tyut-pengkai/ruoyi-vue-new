package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreProductStock;

import java.util.List;

/**
 * 档口商品库存Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductStockService {
    /**
     * 查询档口商品库存
     *
     * @param storeProdStockId 档口商品库存主键
     * @return 档口商品库存
     */
    public StoreProductStock selectStoreProductStockByStoreProdStockId(Long storeProdStockId);

    /**
     * 查询档口商品库存列表
     *
     * @param storeProductStock 档口商品库存
     * @return 档口商品库存集合
     */
    public List<StoreProductStock> selectStoreProductStockList(StoreProductStock storeProductStock);

    /**
     * 新增档口商品库存
     *
     * @param storeProductStock 档口商品库存
     * @return 结果
     */
    public int insertStoreProductStock(StoreProductStock storeProductStock);

    /**
     * 修改档口商品库存
     *
     * @param storeProductStock 档口商品库存
     * @return 结果
     */
    public int updateStoreProductStock(StoreProductStock storeProductStock);

    /**
     * 批量删除档口商品库存
     *
     * @param storeProdStockIds 需要删除的档口商品库存主键集合
     * @return 结果
     */
    public int deleteStoreProductStockByStoreProdStockIds(Long[] storeProdStockIds);

    /**
     * 删除档口商品库存信息
     *
     * @param storeProdStockId 档口商品库存主键
     * @return 结果
     */
    public int deleteStoreProductStockByStoreProdStockId(Long storeProdStockId);
}
