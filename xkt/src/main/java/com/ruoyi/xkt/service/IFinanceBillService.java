package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.finance.FinanceBillInfo;
import com.ruoyi.xkt.dto.order.StoreOrderInfo;
import com.ruoyi.xkt.enums.EPayChannel;

/**
 * @author liangyq
 * @date 2025-04-08 21:14
 */
public interface IFinanceBillService {
    /**
     * 根据订单创建收款单
     *
     * @param orderInfo
     * @param payId
     * @param payChannel
     * @return
     */
    FinanceBillInfo createCollectionBillAfterOrderPaid(StoreOrderInfo orderInfo, Long payId, EPayChannel payChannel);
}
