package com.ruoyi.payment.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.payment.domain.PaymentOrder;
import com.ruoyi.payment.domain.dto.CreateOrderRequest;

/**
 * 支付订单Service接口
 * 
 * @author auto
 * @date 2025-06-24
 */
public interface IPaymentOrderService 
{
    /**
     * 根据订单号查询订单
     *
     * @param orderNo 订单号
     * @return 订单信息
     */
    public PaymentOrder getOrderByOrderNo(String orderNo);

    /**
     * 用户创建订单
     * @param request
     * @return
     */
    public PaymentOrder createOrder(CreateOrderRequest request);

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
     * 批量删除支付订单
     * 
     * @param orderIds 需要删除的支付订单主键集合
     * @return 结果
     */
    public int deletePaymentOrderByOrderIds(Long[] orderIds);

    /**
     * 删除支付订单信息
     * 
     * @param orderId 支付订单主键
     * @return 结果
     */
    public int deletePaymentOrderByOrderId(Long orderId);

    /**
     * 取消订单
     * @param orderId 订单ID
     * @return 结果
     */
    public int cancelOrder(Long orderId);

    /**
     * 处理支付成功后的逻辑,更新套餐订单信息:关联paypal订单ID,支付状态,支付方式,支付时间,支付金额等
     * @param orderNo 订单号
     * @param payParams 支付参数 (包含支付订单ID,支付方式,支付状态,支付时间,支付金额,支付者等)
     * @return PaymentOrder
     */
    public PaymentOrder processPaymentSuccess(String orderNo,Map<String,String> payParams);

    /**
     * 处理支付成功后的逻辑,更新套餐订单信息:关联paypal订单ID,支付状态,支付方式,支付时间,支付金额等
     * @param PaymentOrder 订单号
     * @return PaymentOrder
     */
    public PaymentOrder processPaymentSuccess(PaymentOrder PaymentOrder);
}
