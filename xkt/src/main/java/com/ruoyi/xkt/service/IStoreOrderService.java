package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreOrder;
import com.ruoyi.xkt.dto.order.StoreOrderAddDTO;
import com.ruoyi.xkt.dto.order.StoreOrderAddResult;
import com.ruoyi.xkt.dto.order.StoreOrderInfo;
import com.ruoyi.xkt.dto.order.StoreOrderUpdateDTO;
import com.ruoyi.xkt.enums.EPayChannel;
import com.ruoyi.xkt.enums.EPayPage;

import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-04-02 13:16
 */
public interface IStoreOrderService {
    /**
     * 创建订单
     *
     * @param storeOrderAddDTO 订单信息
     * @param beginPay         是否发起支付
     * @param payChannel       支付渠道
     * @param payPage          支付来源
     * @return
     */
    StoreOrderAddResult createOrder(StoreOrderAddDTO storeOrderAddDTO, boolean beginPay, EPayChannel payChannel,
                                    EPayPage payPage);

    /**
     * 更新订单
     *
     * @param storeOrderUpdateDTO
     * @return
     */
    StoreOrderInfo modifyOrder(StoreOrderUpdateDTO storeOrderUpdateDTO);

    /**
     * 通过订单号获取订单
     *
     * @param orderNo
     * @return
     */
    StoreOrder getByOrderNo(String orderNo);

    /**
     * 准备支付订单
     *
     * @param storeOrderId
     */
    StoreOrderInfo preparePayOrder(Long storeOrderId);

    /**
     * 订单支付成果
     * TODO 更新扣除手续费后的金额
     *
     * @param storeOrderId
     * @param totalAmount
     * @param realTotalAmount
     * @return
     */
    StoreOrderInfo paySuccess(Long storeOrderId, BigDecimal totalAmount, BigDecimal realTotalAmount);
}
