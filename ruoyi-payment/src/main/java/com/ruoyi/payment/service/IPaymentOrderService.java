package com.ruoyi.payment.service;

import java.util.List;
import com.ruoyi.payment.domain.PaymentOrder;

/**
 * 支付订单Service接口
 * 
 * @author auto
 * @date 2025-06-24
 */
public interface IPaymentOrderService 
{
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
     * 处理支付成功逻辑
     *
     * @param orderNo 订单号
     * @return 结果
     */
    public boolean processPaymentSuccess(String orderNo);

    /**
     * 创建支付订单
     *
     * @param userId 用户ID
     * @param packageId 套餐ID
     * @return 支付订单
     */
    public PaymentOrder createPaymentOrder(Long userId, Long packageId);
}
