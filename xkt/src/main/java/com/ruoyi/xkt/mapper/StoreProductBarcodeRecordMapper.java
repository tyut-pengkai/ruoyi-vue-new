package com.ruoyi.xkt.mapper;

import com.ruoyi.xkt.domain.StoreProductBarcodeRecord;

import java.util.List;

/**
 * 档口打印条形码记录Mapper接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface StoreProductBarcodeRecordMapper {
    /**
     * 查询档口打印条形码记录
     *
     * @param storeProdBarcodeRecordId 档口打印条形码记录主键
     * @return 档口打印条形码记录
     */
    public StoreProductBarcodeRecord selectStoreProductBarcodeRecordByStoreProdBarcodeRecordId(Long storeProdBarcodeRecordId);

    /**
     * 查询档口打印条形码记录列表
     *
     * @param storeProductBarcodeRecord 档口打印条形码记录
     * @return 档口打印条形码记录集合
     */
    public List<StoreProductBarcodeRecord> selectStoreProductBarcodeRecordList(StoreProductBarcodeRecord storeProductBarcodeRecord);

    /**
     * 新增档口打印条形码记录
     *
     * @param storeProductBarcodeRecord 档口打印条形码记录
     * @return 结果
     */
    public int insertStoreProductBarcodeRecord(StoreProductBarcodeRecord storeProductBarcodeRecord);

    /**
     * 修改档口打印条形码记录
     *
     * @param storeProductBarcodeRecord 档口打印条形码记录
     * @return 结果
     */
    public int updateStoreProductBarcodeRecord(StoreProductBarcodeRecord storeProductBarcodeRecord);

    /**
     * 删除档口打印条形码记录
     *
     * @param storeProdBarcodeRecordId 档口打印条形码记录主键
     * @return 结果
     */
    public int deleteStoreProductBarcodeRecordByStoreProdBarcodeRecordId(Long storeProdBarcodeRecordId);

    /**
     * 批量删除档口打印条形码记录
     *
     * @param storeProdBarcodeRecordIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoreProductBarcodeRecordByStoreProdBarcodeRecordIds(Long[] storeProdBarcodeRecordIds);
}
