package com.ruoyi.xkt.dto.order;

import lombok.Data;

/**
 * @author liangyq
 * @date 2025-11-22
 */
@Data
public class StoreOrderDetailStatusDTO {
    /**
     * 订单ID
     */
    private Long orderId;
    /**
     * 订单明细ID
     */
    private Long detailId;
    /**
     * 订单明细状态（同订单状态）[10:已取消 11:待付款 12:待发货 13:已发货 14:已完成 21:售后中 22:售后拒绝 23:平台介入 24:售后完成]
     */
    private Integer detailStatus;
    /**
     * 退货订单明细ID
     */
    private Long refundDetailId;
    /**
     * 退货订单明细状态（同订单状态）[10:已取消 11:待付款 12:待发货 13:已发货 14:已完成 21:售后中 22:售后拒绝 23:平台介入 24:售后完成]
     */
    private Integer refundDetailStatus;
}
