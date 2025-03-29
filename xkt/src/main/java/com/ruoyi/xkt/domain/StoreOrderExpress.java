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
 * 档口代发订单快递对象 store_order_express
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreOrderExpress extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 代发订单明细快递信息ID
     */
    @TableId
    private Long id;

    /**
     * 档口代发订单明细ID
     */
    @Excel(name = "档口代发订单明细ID")
    private Long storeOrderDetailId;

    /**
     * 代发订单ID
     */
    @Excel(name = "代发订单ID")
    private Long storeOrderId;

    /**
     * 档口ID
     */
    @Excel(name = "档口ID")
    private Long storeId;

    /**
     * 发货类型（系统订单、自己打单）
     */
    @Excel(name = "发货类型", readConverterExp = "系=统订单、自己打单")
    private Long deliveryType;

    /**
     * 发货物流ID
     */
    @Excel(name = "发货物流ID")
    private Long expressId;

    /**
     * 快递费用
     */
    @Excel(name = "快递费用")
    private BigDecimal expressFee;

    /**
     * 快递单号（如有多个，用逗号相连）
     */
    @Excel(name = "快递单号", readConverterExp = "如=有多个，用逗号相连")
    private String trackingNum;

    /**
     * 发货状态
     */
    @Excel(name = "发货状态")
    private String exprStatus;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeOrderDetailId", getStoreOrderDetailId())
                .append("storeOrderId", getStoreOrderId())
                .append("storeId", getStoreId())
                .append("deliveryType", getDeliveryType())
                .append("expressId", getExpressId())
                .append("expressFee", getExpressFee())
                .append("trackingNum", getTrackingNum())
                .append("exprStatus", getExprStatus())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
