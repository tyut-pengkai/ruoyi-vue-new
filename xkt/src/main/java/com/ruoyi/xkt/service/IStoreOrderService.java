package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.StoreOrder;
import com.ruoyi.xkt.dto.order.StoreOrderAddDTO;
import com.ruoyi.xkt.dto.order.StoreOrderInfo;

/**
 * @author liangyq
 * @date 2025-04-02 13:16
 */
public interface IStoreOrderService {
    /**
     * 创建订单
     *
     * @param storeOrderAddDTO
     * @return
     */
    StoreOrderInfo createOrder(StoreOrderAddDTO storeOrderAddDTO);

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
}
