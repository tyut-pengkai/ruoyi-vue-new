package com.ruoyi.xkt.service;

/**
 * 单据编号Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IVoucherSequenceService {

    /**
     * 获取档口单据编号
     *
     * @param storeId     档口ID
     * @param type        单据类型 STORE_SALE STORAGE  DEMAND STORE_ORDER
     * @param voucherDate 单据日期 默认为当天
     * @return String
     */
    String generateCode(Long storeId, String type, String voucherDate);

}
