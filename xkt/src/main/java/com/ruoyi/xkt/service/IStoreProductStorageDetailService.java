package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreProductStorageDetail;

import java.util.List;

/**
 * 档口商品入库明细Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductStorageDetailService {
    /**
     * 查询档口商品入库明细
     *
     * @param storeProdStorDetailId 档口商品入库明细主键
     * @return 档口商品入库明细
     */
    public StoreProductStorageDetail selectStoreProductStorageDetailByStoreProdStorDetailId(Long storeProdStorDetailId);

    /**
     * 查询档口商品入库明细列表
     *
     * @param storeProductStorageDetail 档口商品入库明细
     * @return 档口商品入库明细集合
     */
    public List<StoreProductStorageDetail> selectStoreProductStorageDetailList(StoreProductStorageDetail storeProductStorageDetail);

    /**
     * 新增档口商品入库明细
     *
     * @param storeProductStorageDetail 档口商品入库明细
     * @return 结果
     */
    public int insertStoreProductStorageDetail(StoreProductStorageDetail storeProductStorageDetail);

    /**
     * 修改档口商品入库明细
     *
     * @param storeProductStorageDetail 档口商品入库明细
     * @return 结果
     */
    public int updateStoreProductStorageDetail(StoreProductStorageDetail storeProductStorageDetail);

    /**
     * 批量删除档口商品入库明细
     *
     * @param storeProdStorDetailIds 需要删除的档口商品入库明细主键集合
     * @return 结果
     */
    public int deleteStoreProductStorageDetailByStoreProdStorDetailIds(Long[] storeProdStorDetailIds);

    /**
     * 删除档口商品入库明细信息
     *
     * @param storeProdStorDetailId 档口商品入库明细主键
     * @return 结果
     */
    public int deleteStoreProductStorageDetailByStoreProdStorDetailId(Long storeProdStorDetailId);
}
