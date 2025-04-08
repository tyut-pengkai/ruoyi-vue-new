package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.order.StoreOrderInfo;
import com.ruoyi.xkt.dto.payment.PaymentBillInfo;

/**
 * @author liangyq
 * @date 2025-04-08 21:14
 */
public interface IPaymentBillService {
    /**
     * 根据订单创建收款单
     *
     * @param orderInfo
     * @return
     */
    PaymentBillInfo createCollectionBillByOrder(StoreOrderInfo orderInfo);
}
