package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.FinanceBill;
import com.ruoyi.xkt.dto.finance.FinanceBillDTO;
import com.ruoyi.xkt.dto.finance.FinanceBillExt;
import com.ruoyi.xkt.dto.order.StoreOrderExt;
import com.ruoyi.xkt.enums.EFinBillSrcType;
import com.ruoyi.xkt.enums.EFinBillStatus;
import com.ruoyi.xkt.enums.EFinBillType;
import com.ruoyi.xkt.enums.EPayChannel;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-08 21:14
 */
public interface IFinanceBillService {
    /**
     * 获取单据
     *
     * @param billNo
     * @return
     */
    FinanceBill getByBillNo(String billNo);

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
     * 订单完成创建转移单（入账）
     *
     * @param orderExt
     * @param afterSaleOrderExts
     * @return
     */
    FinanceBillExt createOrderCompletedTransferBill(StoreOrderExt orderExt, List<StoreOrderExt> afterSaleOrderExts);

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

    /**
     * 充值创建收款单（未入账）
     *
     * @param storeId
     * @param amount
     * @param payChannel
     * @return
     */
    FinanceBillExt createRechargeCollectionBill(Long storeId, BigDecimal amount, EPayChannel payChannel);

    /**
     * 充值收款单入账
     *
     * @param billNo
     */
    void entryRechargeCollectionBill(String billNo);

    /**
     * 内部转移单（已入账）
     *
     * @param inputAccountId
     * @param outputAccountId
     * @param amount
     * @param srcType
     * @param srcId
     * @param relType
     * @param relId
     * @param remark
     * @return
     */
    FinanceBillExt createInternalTransferBill(Long inputAccountId, Long outputAccountId, BigDecimal amount,
                                              Integer srcType, Long srcId, Integer relType, Long relId, String remark);

    /**
     * 单据列表
     *
     * @param billStatus
     * @param billType
     * @param billSrcType
     * @param count
     * @return
     */
    List<FinanceBillDTO> listByStatus(EFinBillStatus billStatus, EFinBillType billType, EFinBillSrcType billSrcType,
                                      Integer count);


}
