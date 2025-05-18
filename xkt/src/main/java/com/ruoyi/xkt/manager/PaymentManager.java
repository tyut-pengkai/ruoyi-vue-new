package com.ruoyi.xkt.manager;

import com.ruoyi.xkt.dto.order.StoreOrderExt;
import com.ruoyi.xkt.dto.order.StoreOrderRefund;
import com.ruoyi.xkt.enums.ENetResult;
import com.ruoyi.xkt.enums.EPayChannel;
import com.ruoyi.xkt.enums.EPayPage;

import java.math.BigDecimal;
import java.util.Date;

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
     * 支付
     *
     * @param tradeNo
     * @param amount
     * @param subject
     * @param payPage
     * @param expireTime
     * @return 跳转页面数据/签名字符串/支付跳转链接/预支付交易会话标识（根据支付渠道&支付来源确定）
     */
    String pay(String tradeNo, BigDecimal amount, String subject, EPayPage payPage, Date expireTime);

    /**
     * 订单支付
     *
     * @param order
     * @param payPage
     * @return 跳转页面数据/签名字符串/支付跳转链接/预支付交易会话标识（根据支付渠道&支付来源确定）
     */
    String payStoreOrder(StoreOrderExt order, EPayPage payPage);

    /**
     * 订单退款
     *
     * @param orderRefund
     */
    void refundStoreOrder(StoreOrderRefund orderRefund);

    /**
     * 是否已支付
     * 此方法通常用来判否，如果返回true还需要核对支付金额等相关信息才可确定是否完成支付！！
     *
     * @param orderNo 订单号
     * @return
     */
    ENetResult queryStoreOrderPayResult(String orderNo);

    /**
     * 转账
     *
     * @param bizNo
     * @param identity
     * @param realName
     * @param amount
     * @return
     */
    boolean transfer(String bizNo, String identity, String realName, BigDecimal amount);

    /**
     * 转账结果
     *
     * @param bizNo
     * @return
     */
    ENetResult queryTransferResult(String bizNo);

}
