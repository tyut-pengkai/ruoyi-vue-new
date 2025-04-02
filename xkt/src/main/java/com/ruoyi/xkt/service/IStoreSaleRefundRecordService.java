package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.storeSaleRefundRecord.StoreSaleRefundRecordDTO;

import java.util.List;

/**
 * 档口销售返单Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreSaleRefundRecordService {

    /**
     * 查询档口销售返单列表
     *
     * @param storeId     档口ID
     * @param storeSaleId 档口销售ID
     * @return 档口销售返单集合
     */
    public List<StoreSaleRefundRecordDTO> selectList(Long storeId, Long storeSaleId);
}
