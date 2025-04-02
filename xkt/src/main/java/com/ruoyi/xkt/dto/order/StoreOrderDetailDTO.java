package com.ruoyi.xkt.dto.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 代发订单明细
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.582
 **/
@Data
public class StoreOrderDetailDTO {
    /**
     * 订单明细ID
     */
    private Long id;
    /**
     * 订单ID
     */
    private Long storeOrderId;
    /**
     * 商品颜色尺码ID
     */
    private Long storeProdColorSizeId;
    /**
     * 商品ID
     */
    private Long storeProdId;
    /**
     * 订单明细状态（同订单状态）[10:已取消 11:待付款 12:待发货 13:已发货 14:已完成 21:售后中 22:售后拒绝 23:平台介入 24:售后完成]
     */
    private Integer detailStatus;
    /**
     * 支付状态[1:初始 2:支付中 3:已支付]
     */
    private Integer payStatus;
    /**
     * 物流ID
     */
    private Long expressId;
    /**
     * 物流类型[1:平台物流 2:档口物流]
     */
    private Integer expressType;
    /**
     * 物流状态[1:初始 2:下单中 3:已下单 4:取消中 5:已揽件 6:拦截中 99:已结束]
     */
    private Integer expressStatus;
    /**
     * 物流请求单号
     */
    private String expressReqNo;
    /**
     * 物流运单号（快递单号），档口/用户自己填写时可能存在多个，使用“,”分割
     */
    private String expressWaybillNo;
    /**
     * 商品单价
     */
    private BigDecimal goodsPrice;
    /**
     * 商品数量
     */
    private Integer goodsQuantity;
    /**
     * 商品金额（商品单价*商品数量）
     */
    private BigDecimal goodsAmount;
    /**
     * 快递费
     */
    private BigDecimal expressFee;
    /**
     * 总金额（商品金额+快递费）
     */
    private BigDecimal totalAmount;
    /**
     * 实际总金额（总金额-支付渠道服务费）
     */
    private BigDecimal realTotalAmount;
    /**
     * 退货原订单明细ID
     */
    private Long refundOrderDetailId;
    /**
     * 退货原因
     */
    private Integer refundReasonCode;
    /**
     * 退货拒绝原因
     */
    private String refundRejectReason;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 版本号
     */
    private Integer version;
}
