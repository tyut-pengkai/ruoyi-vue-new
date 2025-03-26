package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreSaleRefundRecord;

import java.util.List;

/**
 * 档口销售返单Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreSaleRefundRecordService {
    /**
     * 查询档口销售返单
     *
     * @param storeSaleRefundRecordId 档口销售返单主键
     * @return 档口销售返单
     */
    public StoreSaleRefundRecord selectStoreSaleRefundRecordByStoreSaleRefundRecordId(Long storeSaleRefundRecordId);

    /**
     * 查询档口销售返单列表
     *
     * @param storeSaleRefundRecord 档口销售返单
     * @return 档口销售返单集合
     */
    public List<StoreSaleRefundRecord> selectStoreSaleRefundRecordList(StoreSaleRefundRecord storeSaleRefundRecord);

    /**
     * 新增档口销售返单
     *
     * @param storeSaleRefundRecord 档口销售返单
     * @return 结果
     */
    public int insertStoreSaleRefundRecord(StoreSaleRefundRecord storeSaleRefundRecord);

    /**
     * 修改档口销售返单
     *
     * @param storeSaleRefundRecord 档口销售返单
     * @return 结果
     */
    public int updateStoreSaleRefundRecord(StoreSaleRefundRecord storeSaleRefundRecord);

    /**
     * 批量删除档口销售返单
     *
     * @param storeSaleRefundRecordIds 需要删除的档口销售返单主键集合
     * @return 结果
     */
    public int deleteStoreSaleRefundRecordByStoreSaleRefundRecordIds(Long[] storeSaleRefundRecordIds);

    /**
     * 删除档口销售返单信息
     *
     * @param storeSaleRefundRecordId 档口销售返单主键
     * @return 结果
     */
    public int deleteStoreSaleRefundRecordByStoreSaleRefundRecordId(Long storeSaleRefundRecordId);
}
