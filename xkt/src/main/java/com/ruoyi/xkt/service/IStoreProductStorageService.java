package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreProductStorage;

import java.util.List;

/**
 * 档口商品入库Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductStorageService {
    /**
     * 查询档口商品入库
     *
     * @param storeProdStorId 档口商品入库主键
     * @return 档口商品入库
     */
    public StoreProductStorage selectStoreProductStorageByStoreProdStorId(Long storeProdStorId);

    /**
     * 查询档口商品入库列表
     *
     * @param storeProductStorage 档口商品入库
     * @return 档口商品入库集合
     */
    public List<StoreProductStorage> selectStoreProductStorageList(StoreProductStorage storeProductStorage);

    /**
     * 新增档口商品入库
     *
     * @param storeProductStorage 档口商品入库
     * @return 结果
     */
    public int insertStoreProductStorage(StoreProductStorage storeProductStorage);

    /**
     * 修改档口商品入库
     *
     * @param storeProductStorage 档口商品入库
     * @return 结果
     */
    public int updateStoreProductStorage(StoreProductStorage storeProductStorage);

    /**
     * 批量删除档口商品入库
     *
     * @param storeProdStorIds 需要删除的档口商品入库主键集合
     * @return 结果
     */
    public int deleteStoreProductStorageByStoreProdStorIds(Long[] storeProdStorIds);

    /**
     * 删除档口商品入库信息
     *
     * @param storeProdStorId 档口商品入库主键
     * @return 结果
     */
    public int deleteStoreProductStorageByStoreProdStorId(Long storeProdStorId);
}
