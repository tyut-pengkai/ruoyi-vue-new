package com.ruoyi.payment.model;

/**
 * 支付结果封装类
 */
public class PaymentResult {
    
    /** 是否成功 */
    private boolean success;
    
    /** 错误代码 */
    private String errorCode;
    
    /** 错误信息 */
    private String errorMsg;
    
    /** 支付渠道 */
    private String channel;
    
    /** 支付渠道交易ID */
    private String transactionId;
    
    /** 支付状态 */
    private String status;
    
    /** 重定向URL */
    private String redirectUrl;
    
    /** 响应数据 */
    private Object data;

    public static PaymentResult success() {
        PaymentResult result = new PaymentResult();
        result.setSuccess(true);
        return result;
    }

    public static PaymentResult success(String transactionId, String status) {
        PaymentResult result = new PaymentResult();
        result.setSuccess(true);
        result.setTransactionId(transactionId);
        result.setStatus(status);
        return result;
    }

    public static PaymentResult success(String transactionId, String status, Object data) {
        PaymentResult result = new PaymentResult();
        result.setSuccess(true);
        result.setTransactionId(transactionId);
        result.setStatus(status);
        result.setData(data);
        return result;
    }

    public static PaymentResult redirect(String redirectUrl) {
        PaymentResult result = new PaymentResult();
        result.setSuccess(true);
        result.setRedirectUrl(redirectUrl);
        return result;
    }

    public static PaymentResult fail(String errorCode, String errorMsg) {
        PaymentResult result = new PaymentResult();
        result.setSuccess(false);
        result.setErrorCode(errorCode);
        result.setErrorMsg(errorMsg);
        return result;
    }

    // 标准的 getter 和 setter 方法，支持链式调用
    public boolean isSuccess() {
        return success;
    }

    public PaymentResult setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public PaymentResult setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public PaymentResult setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    public String getChannel() {
        return channel;
    }

    public PaymentResult setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public PaymentResult setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public PaymentResult setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public PaymentResult setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
        return this;
    }

    public Object getData() {
        return data;
    }

    public PaymentResult setData(Object data) {
        this.data = data;
        return this;
    }
} 