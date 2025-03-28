package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 档口代发订单对象 store_order
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreOrder extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口代发订单ID
     */
    @TableId
    private Long storeOrderId;

    /**
     * 档口id
     */
    @Excel(name = "档口id")
    private Long storeId;

    /**
     * 下单用户ID sys_user.id
     */
    @Excel(name = "下单用户ID sys_user.id")
    private Long userId;

    /**
     * 代发订单CODE
     */
    @Excel(name = "代发订单CODE")
    private String code;

    /**
     * 订单类型（普通销售、销售退货）
     */
    @Excel(name = "订单类型", readConverterExp = "普=通销售、销售退货")
    private Long orderType;

    /**
     * 退货时，对应的原订单ID
     */
    @Excel(name = "退货时，对应的原订单ID")
    private Long refundOriginalId;

    /**
     * 下单发货的快递
     */
    @Excel(name = "下单发货的快递")
    private Long expressId;

    /**
     * 快递费
     */
    @Excel(name = "快递费")
    private BigDecimal expressFee;

    /**
     * 发货方式（货齐再发、有货先发）
     */
    @Excel(name = "发货方式", readConverterExp = "货=齐再发、有货先发")
    private Long deliveryType;

    /**
     * 发货最晚时间（精确到小时）
     */
    @Excel(name = "发货最晚时间", readConverterExp = "精=确到小时")
    private Date deliveryEndTime;

    /**
     * 退货原因
     */
    @Excel(name = "退货原因")
    private Long refundReasonId;

    /**
     * 商品数量
     */
    @Excel(name = "商品数量")
    private Integer quantity;

    /**
     * 订单金额
     */
    @Excel(name = "订单金额")
    private BigDecimal amount;

    /**
     * 拒绝原因
     */
    @Excel(name = "拒绝原因")
    private String rejectReason;

    /**
     * 凭证日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "凭证日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voucherDate;

    /**
     * 订单状态
     */
    @Excel(name = "订单状态")
    private String orderStatus;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("storeOrderId", getStoreOrderId())
                .append("storeId", getStoreId())
                .append("userId", getUserId())
                .append("code", getCode())
                .append("orderType", getOrderType())
                .append("refundOriginalId", getRefundOriginalId())
                .append("expressId", getExpressId())
                .append("expressFee", getExpressFee())
                .append("deliveryType", getDeliveryType())
                .append("deliveryEndTime", getDeliveryEndTime())
                .append("refundReasonId", getRefundReasonId())
                .append("quantity", getQuantity())
                .append("amount", getAmount())
                .append("rejectReason", getRejectReason())
                .append("voucherDate", getVoucherDate())
                .append("orderStatus", getOrderStatus())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
