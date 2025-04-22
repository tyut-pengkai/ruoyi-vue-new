package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.finance.FinanceBillExt;
import com.ruoyi.xkt.dto.order.StoreOrderExt;
import com.ruoyi.xkt.enums.EPayChannel;

import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-04-08 21:14
 */
public interface IFinanceBillService {
    /**
     * 订单支付完成创建收款单（入账）
     *
     * @param orderExt
     * @param payId
     * @param payChannel
     * @return
     */
    FinanceBillExt createOrderPaidCollectionBill(StoreOrderExt orderExt, Long payId, EPayChannel payChannel);

    /**
     * 订单完成创建转移单（入账）
     *
     * @param orderExt
     * @return
     */
    FinanceBillExt createOrderCompletedTransferBill(StoreOrderExt orderExt);

    /**
     * 售后订单创建付款单（未入账）
     *
     * @param orderExt
     * @return
     */
    FinanceBillExt createRefundOrderPaymentBill(StoreOrderExt orderExt);

    /**
     * 售后订单付款单入账
     *
     * @param storeOrderId
     */
    void entryRefundOrderPaymentBill(Long storeOrderId);

    /**
     * 提现创建付款单（未入账）
     *
     * @param storeId
     * @param amount
     * @param payChannel
     * @return
     */
    FinanceBillExt createWithdrawPaymentBill(Long storeId, BigDecimal amount, EPayChannel payChannel);

    /**
     * 提现付款单入账
     *
     * @param financeBillId
     */
    void entryWithdrawPaymentBill(Long financeBillId);


}
