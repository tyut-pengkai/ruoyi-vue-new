package com.ruoyi.payment.domain.dto;

import java.math.BigDecimal;

/**
 * 创建订单请求DTO
 */
public class CreateOrderRequest {
    /** 套餐ID */
    private Long packageId;

    /** 设备ID */
    private Long deviceId;

    /** 支付方式 */
    private String paymentMethod;

    /** 描述 */
    private String description;

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
 