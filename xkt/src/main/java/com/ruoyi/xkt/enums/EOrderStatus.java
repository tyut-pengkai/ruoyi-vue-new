package com.ruoyi.xkt.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liangyq
 * @date 2025-04-02 23:42
 */
@Getter
@AllArgsConstructor
public enum EOrderStatus {

    //销售订单状态
    CANCELLED(10, "已取消"),
    PENDING_PAYMENT(11, "待付款"),
    PENDING_SHIPMENT(12, "待发货"),
    SHIPPED(13, "已发货"),
    COMPLETED(14, "已完成"),

    //售后订单状态
    AFTER_SALE_IN_PROGRESS(21, "售后中"),
    AFTER_SALE_REJECTED(22, "售后拒绝"),
    PLATFORM_INTERVENED(23, "平台介入"),
    AFTER_SALE_COMPLETED(24, "售后完成");

    private final Integer value;
    private final String label;

    public static EOrderStatus of(Integer value) {
        for (EOrderStatus e : EOrderStatus.values()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }
}
