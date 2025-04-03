package com.ruoyi.xkt.service;

import com.ruoyi.xkt.dto.order.StoreOrderAddDTO;
import com.ruoyi.xkt.dto.order.StoreOrderInfoDTO;

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
    StoreOrderInfoDTO createOrder(StoreOrderAddDTO storeOrderAddDTO);
}
