package com.ruoyi.xkt.dto.order;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-07-23
 */
@Data
public class StoreOrderCountDTO {
    /**
     * 全部订单
     */
    private Integer all;
    /**
     * 售后订单（退款/售后）
     */
    private Integer afterSale;
    /**
     * 已取消
     */
    private Integer cancelled;
    /**
     * 待付款
     */
    private Integer pendingPayment;
    /**
     * 待发货
     */
    private Integer pendingShipment;
    /**
     * 已发货
     */
    private Integer shipped;
    /**
     * 已完成
     */
    private Integer completed;
    /**
     * 售后中
     */
    private Integer afterSaleInProgress;
    /**
     * 售后拒绝
     */
    private Integer afterSaleRejected;
    /**
     * 平台介入
     */
    private Integer platformIntervened;
    /**
     * 售后完成
     */
    private Integer afterSaleCompleted;
}
