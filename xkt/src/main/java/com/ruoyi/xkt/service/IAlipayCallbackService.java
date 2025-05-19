package com.ruoyi.xkt.service;

import com.ruoyi.xkt.domain.AlipayCallback;

import java.util.List;

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
    void processOrderPaid(AlipayCallback info);

    /**
     * 处理充值结果
     *
     * @param info
     */
    void processRecharge(AlipayCallback info);

    /**
     * 标记为不处理
     *
     * @param info
     */
    void noNeedProcess(AlipayCallback info);

    /**
     * 获取需要继续处理的回调
     *
     * @param count
     * @return
     */
    List<AlipayCallback> listNeedContinueProcessCallback(Integer count);

    /**
     * 继续处理回调
     *
     * @param infoList
     */
    void continueProcess(List<AlipayCallback> infoList);
}
