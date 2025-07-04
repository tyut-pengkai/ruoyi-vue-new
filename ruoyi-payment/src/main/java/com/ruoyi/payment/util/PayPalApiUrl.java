package com.ruoyi.payment.util;

/**
 * PayPal API URL常量
 */
public class PayPalApiUrl {
    
    /** 沙箱环境 - 获取访问令牌URL */
    public static final String SANDBOX_ACCESS_TOKEN_URL = "https://api-m.sandbox.paypal.com/v1/oauth2/token";
    
    /** 正式环境 - 获取访问令牌URL */
    public static final String LIVE_ACCESS_TOKEN_URL = "https://api-m.paypal.com/v1/oauth2/token";
    
    /** 沙箱环境 - 订单API URL */
    public static final String SANDBOX_ORDERS_URL = "https://api-m.sandbox.paypal.com/v2/checkout/orders";
    
    /** 正式环境 - 订单API URL */
    public static final String LIVE_ORDERS_URL = "https://api-m.paypal.com/v2/checkout/orders";
} 