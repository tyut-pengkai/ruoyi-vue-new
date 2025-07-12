package com.ruoyi.payment.strategy;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ruoyi.payment.domain.PaymentOrder;
import com.ruoyi.payment.model.PaymentResponse;
import com.ruoyi.payment.model.PaymentResult;

/**
 * 支付策略接口
 */
public interface PaymentStrategy {
    
    /**
     * 处理支付
     * 
     * @param order 支付订单
     * @return 支付响应
     */
    PaymentResponse processPayment(PaymentOrder order);
    
 
    /**
     * 处理支付回调
     * 
     * @param params 回调参数
     * @return 处理结果
     */
    PaymentResponse handleCallbackSuccess(Map<String, String> params);
    
    /**
     * 处理支付回调
     * 
     * @param request HTTP请求
     * @return 处理结果
     */
    PaymentResponse handleCallbackSuccess(HttpServletRequest request);
    
    /**
     * 处理支付取消回调
     * 
     * @param request HTTP请求
     * @return 处理结果
     */
    PaymentResponse handleCallbackCancel(Map<String, String> params) ;
    
    
    /**
     * 完成支付
     * 
     * @param params 支付参数
     * @return 支付结果
     */
    PaymentResult completePayment(Map<String, Object> params);
    
    /**
     * 创建支付
     * 
     * @param params 支付参数
     * @return 支付结果
     */
    PaymentResult createPayment(Map<String, Object> params);
    
    /**
     * 执行支付
     * 
     * @param params 支付参数
     * @return 支付结果
     */
    PaymentResult executePayment(Map<String, Object> params);
    
    /**
     * 查询支付状态
     * 
     * @param orderId 订单ID
     * @return 查询结果
     */
    PaymentResult queryPaymentStatus(String orderId);

    /**
     * 获取支付状态
     * 
     * @param params 查询参数
     * @return 支付结果
     */
    PaymentResult getPaymentStatus(Map<String, Object> params);

    /**
     * 获取支付状态
     * 
     * @param paymentId 支付ID
     * @return 支付结果
     */
    PaymentResult getPaymentStatus(String paymentId);
} 