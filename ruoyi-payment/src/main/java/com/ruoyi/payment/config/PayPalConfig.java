package com.ruoyi.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * PayPal配置类
 */
@Configuration
@ConfigurationProperties(prefix = "payment.paypal")
public class PayPalConfig {
    
    /**
     * 客户端ID
     */
    private String clientId;
    
    /**
     * 客户端密钥
     */
    private String clientSecret;
    
    /**
     * 是否沙箱环境
     */
    private boolean sandbox = true;
    
    /**
     * 支付成功回调URL
     */
    private String successUrl;
    
    /**
     * 支付取消回调URL
     */
    private String cancelUrl;
    
    /**
     * 通知URL
     */
    private String notifyUrl;
    
    public String getClientId() {
        return clientId;
    }
    
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    
    public String getClientSecret() {
        return clientSecret;
    }
    
    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }
    
    public boolean isSandbox() {
        return sandbox;
    }
    
    public void setSandbox(boolean sandbox) {
        this.sandbox = sandbox;
    }
    
    public String getSuccessUrl() {
        return successUrl;
    }
    
    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }
    
    public String getCancelUrl() {
        return cancelUrl;
    }
    
    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }
    
    public String getNotifyUrl() {
        return notifyUrl;
    }
    
    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }
} 