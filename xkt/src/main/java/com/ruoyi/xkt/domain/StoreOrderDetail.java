package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 档口代发订单明细对象 store_order_detail
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreOrderDetail extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 代发订单明细ID
     */
    @TableId
    private Long id;

    /**
     * 代发订单ID
     */
    @Excel(name = "代发订单ID")
    private Long storeOrderId;

    /**
     * 退货时对应的store_order_detail_id
     */
    @Excel(name = "退货时对应的store_order_detail_id")
    private Long storeOrderDetailRefundId;

    /**
     * 档口商品颜色尺码ID
     */
    @Excel(name = "档口商品颜色尺码ID")
    private Long storeProdColorSizeId;

    /**
     * 档口商品ID
     */
    @Excel(name = "档口商品ID")
    private Long storeProdId;

    /**
     * 档口ID
     */
    @Excel(name = "档口ID")
    private Long storeId;

    /**
     * 商品单价
     */
    @Excel(name = "商品单价")
    private BigDecimal price;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private Integer quantity;

    /**
     * 总金额
     */
    @Excel(name = "总金额")
    private BigDecimal amount;

    /**
     * 明细状态
     */
    @Excel(name = "明细状态")
    private String detailStatus;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeOrderId", getStoreOrderId())
                .append("storeOrderDetailRefundId", getStoreOrderDetailRefundId())
                .append("storeProdColorSizeId", getStoreProdColorSizeId())
                .append("storeProdId", getStoreProdId())
                .append("storeId", getStoreId())
                .append("price", getPrice())
                .append("quantity", getQuantity())
                .append("amount", getAmount())
                .append("detailStatus", getDetailStatus())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
