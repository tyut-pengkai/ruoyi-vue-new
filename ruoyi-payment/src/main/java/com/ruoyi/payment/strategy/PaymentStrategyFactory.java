package com.ruoyi.payment.strategy;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ruoyi.payment.strategy.impl.PayPalPaymentStrategy;

import javax.annotation.PostConstruct;

/**
 * 支付策略工厂
 * 用于获取不同的支付策略实现
 */
@Component
public class PaymentStrategyFactory {

    private final Map<String, PaymentStrategy> strategies = new HashMap<>();
    
    @Autowired
    private PayPalPaymentStrategy payPalStrategy;
    
    @PostConstruct
    public void init() {
        // 注册PayPal支付策略
        strategies.put("paypal", payPalStrategy);
        // 可以在这里注册更多的支付策略
    }
    
    /**
     * 根据支付方式获取对应的支付策略
     * 
     * @param paymentMethod 支付方式，如paypal等
     * @return 对应的支付策略实现
     */
    public PaymentStrategy getStrategy(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            throw new IllegalArgumentException("支付方式不能为空");
        }
        
        paymentMethod = paymentMethod.toLowerCase();
        
        PaymentStrategy strategy = strategies.get(paymentMethod);
        if (strategy == null) {
            throw new IllegalArgumentException("不支持的支付方式: " + paymentMethod);
        }
        
        return strategy;
    }
} 