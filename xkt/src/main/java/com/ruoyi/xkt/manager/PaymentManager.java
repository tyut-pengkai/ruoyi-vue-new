package com.ruoyi.xkt.manager;

import com.ruoyi.xkt.enums.EPayChannel;
import com.ruoyi.xkt.enums.EPayFrom;

/**
 * @author liangyq
 * @date 2025-04-06 19:36
 */
public interface PaymentManager {
    /**
     * 支付渠道
     *
     * @return
     */
    EPayChannel channel();

    /**
     * 订单支付
     *
     * @param storeOrderId
     * @param payFrom
     * @return 跳转页面数据/签名字符串/支付跳转链接/预支付交易会话标识（根据支付渠道&支付来源确定）
     */
    String payForOrder(Long storeOrderId, EPayFrom payFrom);

}
