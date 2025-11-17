package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.storeSaleDetail.StoreTodaySaleDTO;
import com.ruoyi.xkt.dto.storeSaleDetail.StoreTodaySaleSummaryDTO;

/**
 * 档口销售明细Service接口
 *
 * @author ruoyi
 * @date 2025-03-26
 */
public interface IStoreSaleDetailService {

    /**
     * 获取当前档口今日销售数据
     *
     * @param storeId 档口ID
     * @return StoreTodaySaleDTO
     */
    StoreTodaySaleDTO getTodaySale(Long storeId);

    /**
     * 查询今日统计数据
     *
     * @param storeId 档口ID
     * @return StoreTodaySaleSummaryDTO
     */
    StoreTodaySaleSummaryDTO getTodaySaleSummary(Long storeId);

}
