package com.ruoyi.payment.mapper;

import java.util.List;
import com.ruoyi.payment.domain.PaymentOrder;

/**
 * 支付订单Mapper接口
 * 
 * @author auto
 * @date 2025-06-24
 */
public interface PaymentOrderMapper 
{
    /**
     * 根据订单号查询支付订单
     *
     * @param orderNo 支付订单号
     * @return 支付订单
     */
    public PaymentOrder selectPaymentOrderByOrderNo(String orderNo);

    /**
     * 查询支付订单
     * 
     * @param orderId 支付订单主键
     * @return 支付订单
     */
    public PaymentOrder selectPaymentOrderByOrderId(Long orderId);

    /**
     * 查询支付订单列表
     * 
     * @param paymentOrder 支付订单
     * @return 支付订单集合
     */
    public List<PaymentOrder> selectPaymentOrderList(PaymentOrder paymentOrder);

    /**
     * 新增支付订单
     * 
     * @param paymentOrder 支付订单
     * @return 结果
     */
    public int insertPaymentOrder(PaymentOrder paymentOrder);

    /**
     * 修改支付订单
     * 
     * @param paymentOrder 支付订单
     * @return 结果
     */
    public int updatePaymentOrder(PaymentOrder paymentOrder);

    /**
     * 删除支付订单
     * 
     * @param orderId 支付订单主键
     * @return 结果
     */
    public int deletePaymentOrderByOrderId(Long orderId);

    /**
     * 批量删除支付订单
     * 
     * @param orderIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePaymentOrderByOrderIds(Long[] orderIds);

    /**
     * 查询支付订单
     * 
     * @param token PayPal Token
     * @return 支付订单
     */
    public PaymentOrder selectPaymentOrderByToken(String token);

    /**
     * 根据PayPal支付ID查询支付订单
     * 
     * @param paymentId PayPal支付ID
     * @return 支付订单信息
     */
    public PaymentOrder selectPaymentOrderByPaymentId(String paymentId);
}
