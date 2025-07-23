package com.ruoyi.web.controller.xkt.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liangyq
 * @date 2025-07-23
 */
@ApiModel
@Data
public class StoreOrderCountVO {
    /**
     * 全部订单
     */
    @ApiModelProperty(value = "全部订单")
    private Integer all;
    /**
     * 售后订单（退款/售后）
     */
    @ApiModelProperty(value = "售后订单（退款/售后）")
    private Integer afterSale;
    /**
     * 已取消
     */
    @ApiModelProperty(value = "已取消")
    private Integer cancelled;
    /**
     * 待付款
     */
    @ApiModelProperty(value = "待付款")
    private Integer pendingPayment;
    /**
     * 待发货
     */
    @ApiModelProperty(value = "待发货")
    private Integer pendingShipment;
    /**
     * 已发货
     */
    @ApiModelProperty(value = "已发货")
    private Integer shipped;
    /**
     * 已完成
     */
    @ApiModelProperty(value = "已完成")
    private Integer completed;
    /**
     * 售后中
     */
    @ApiModelProperty(value = "售后中")
    private Integer afterSaleInProgress;
    /**
     * 售后拒绝
     */
    @ApiModelProperty(value = "售后拒绝")
    private Integer afterSaleRejected;
    /**
     * 平台介入
     */
    @ApiModelProperty(value = "平台介入")
    private Integer platformIntervened;
    /**
     * 售后完成
     */
    @ApiModelProperty(value = "售后完成")
    private Integer afterSaleCompleted;
}
