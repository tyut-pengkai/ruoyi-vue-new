package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.storeProductStorageDemandDeduct.StoreProdStorageDemandDeductDTO;

/**
 * 档口商品入库抵扣需求Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreProductStorageDemandDeducteService {

    /**
     * 入库单列表获取抵扣需求明细列表
     *
     * @param storeId     档口ID
     * @param storageCode 入库单号
     * @return List<StoreProdStorageDemandDeductDTO>
     */
    StoreProdStorageDemandDeductDTO getStorageDemandDeductList(Long storeId, String storageCode);

}
