package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.AlipayCallback;

/**
 * @author liangyq
 * @date 2025-04-08 17:39
 */
public interface IAlipayCallbackService {

    /**
     * 通过通知ID获取回调信息
     *
     * @param notifyId
     * @return
     */
    AlipayCallback getByNotifyId(String notifyId);

    /**
     * 保存回调信息
     *
     * @param alipayCallback
     * @return
     */
    int insertAlipayCallback(AlipayCallback alipayCallback);

    /**
     * 处理订单支付结果
     *
     * @param info
     */
    void processOrderPay(AlipayCallback info);
}
