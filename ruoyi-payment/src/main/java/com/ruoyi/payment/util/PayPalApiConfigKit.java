package com.ruoyi.payment.util;

import com.ijpay.paypal.PayPalApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PayPal API配置工具类
 * 使用ThreadLocal存储API配置
 */
public class PayPalApiConfigKit {
    
    private static final Logger log = LoggerFactory.getLogger(PayPalApiConfigKit.class);
    
    private static final ThreadLocal<PayPalApiConfig> TL = new ThreadLocal<>();
    
    /**
     * 设置线程本地的 API 配置
     * @param apiConfig API配置
     * @return API配置
     */
    public static PayPalApiConfig setThreadLocalApiConfig(PayPalApiConfig apiConfig) {
        TL.set(apiConfig);
        return apiConfig;
    }
    
    /**
     * 获取线程本地的 API 配置
     * @return API配置
     */
    public static PayPalApiConfig getApiConfig() {
        PayPalApiConfig apiConfig = TL.get();
        if (apiConfig == null) {
            throw new IllegalStateException("PayPalApiConfig未在当前线程中初始化");
        }
        return apiConfig;
    }
    
    /**
     * 移除线程本地的 API 配置
     */
    public static void removeThreadLocalApiConfig() {
        TL.remove();
    }
} 