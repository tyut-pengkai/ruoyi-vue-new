package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreProductStorageDemandDeducte;

import java.util.List;

/**
 * 档口商品入库抵扣需求Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductStorageDemandDeducteService {
    /**
     * 查询档口商品入库抵扣需求
     *
     * @param storeProdStorDemaDeducteId 档口商品入库抵扣需求主键
     * @return 档口商品入库抵扣需求
     */
    public StoreProductStorageDemandDeducte selectStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteId(Long storeProdStorDemaDeducteId);

    /**
     * 查询档口商品入库抵扣需求列表
     *
     * @param storeProductStorageDemandDeducte 档口商品入库抵扣需求
     * @return 档口商品入库抵扣需求集合
     */
    public List<StoreProductStorageDemandDeducte> selectStoreProductStorageDemandDeducteList(StoreProductStorageDemandDeducte storeProductStorageDemandDeducte);

    /**
     * 新增档口商品入库抵扣需求
     *
     * @param storeProductStorageDemandDeducte 档口商品入库抵扣需求
     * @return 结果
     */
    public int insertStoreProductStorageDemandDeducte(StoreProductStorageDemandDeducte storeProductStorageDemandDeducte);

    /**
     * 修改档口商品入库抵扣需求
     *
     * @param storeProductStorageDemandDeducte 档口商品入库抵扣需求
     * @return 结果
     */
    public int updateStoreProductStorageDemandDeducte(StoreProductStorageDemandDeducte storeProductStorageDemandDeducte);

    /**
     * 批量删除档口商品入库抵扣需求
     *
     * @param storeProdStorDemaDeducteIds 需要删除的档口商品入库抵扣需求主键集合
     * @return 结果
     */
    public int deleteStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteIds(Long[] storeProdStorDemaDeducteIds);

    /**
     * 删除档口商品入库抵扣需求信息
     *
     * @param storeProdStorDemaDeducteId 档口商品入库抵扣需求主键
     * @return 结果
     */
    public int deleteStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteId(Long storeProdStorDemaDeducteId);
}
