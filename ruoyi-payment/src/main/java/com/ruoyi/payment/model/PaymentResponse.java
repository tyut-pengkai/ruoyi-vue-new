package com.ruoyi.payment.model;

import java.util.Map;

/**
 * 支付响应类
 */
public class PaymentResponse {
    /** 是否成功 */
    private boolean success;
    
    /** 消息 */
    private String message;
    
    /** 数据 */
    private Map<String, Object> data;

    /**
     * 构造方法
     */
    public PaymentResponse() {
    }

    /**
     * 构造方法
     * 
     * @param success 是否成功
     * @param message 消息
     * @param data 数据
     */
    public PaymentResponse(boolean success, String message, Map<String, Object> data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * 获取是否成功
     * 
     * @return 是否成功
     */
    public boolean isSuccess() {
        return success;
    }

    /**
     * 设置是否成功
     * 
     * @param success 是否成功
     */
    public void setSuccess(boolean success) {
        this.success = success;
    }

    /**
     * 获取消息
     * 
     * @return 消息
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置消息
     * 
     * @param message 消息
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取数据
     * 
     * @return 数据
     */
    public Map<String, Object> getData() {
        return data;
    }

    /**
     * 设置数据
     * 
     * @param data 数据
     */
    public void setData(Map<String, Object> data) {
        this.data = data;
    }
} 