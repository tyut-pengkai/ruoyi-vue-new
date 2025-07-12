package com.ruoyi.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

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
    private String returnUrl;
    
    /**
     * 支付取消回调URL
     */
    private String cancelUrl;
    
    /**
     * 通知URL
     */
    private String notifyUrl;
    
    /**
     * Webhook ID
     */
    private String webhookId;
    
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
    
    public String getReturnUrl() {
        return returnUrl;
    }
    
    public void setReturnUrl(String successUrl) {
        this.returnUrl = successUrl;
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

    public String getWebhookId() {
        return webhookId;
    }

    public void setWebhookId(String webhookId) {
        this.webhookId = webhookId;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
} 