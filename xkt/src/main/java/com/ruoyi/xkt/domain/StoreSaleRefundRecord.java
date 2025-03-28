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
 * 档口销售返单对象 store_sale_refund_record
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreSaleRefundRecord extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口销售出库返单记录ID
     */
    @TableId
    private Long storeSaleRefundRecordId;

    /**
     * 档口销售出库ID
     */
    @Excel(name = "档口销售出库ID")
    private Long storeSaleId;

    /**
     * 档口ID
     */
    @Excel(name = "档口ID")
    private Long storeId;

    /**
     * 档口销售客户ID
     */
    @Excel(name = "档口销售客户ID")
    private Long storeCusId;

    /**
     * 档口销售商品ID
     */
    @Excel(name = "档口销售商品ID")
    private Long storeProdId;

    /**
     * 销售类型（销售、退货、销售/退货）
     */
    @Excel(name = "销售类型", readConverterExp = "销=售、退货、销售/退货")
    private Long saleType;

    /**
     * 单据编号
     */
    @Excel(name = "单据编号")
    private String code;

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
     * 支付方式
     */
    @Excel(name = "支付方式")
    private Long payWay;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("storeSaleRefundRecordId", getStoreSaleRefundRecordId())
                .append("storeSaleId", getStoreSaleId())
                .append("storeId", getStoreId())
                .append("storeCusId", getStoreCusId())
                .append("storeProdId", getStoreProdId())
                .append("saleType", getSaleType())
                .append("code", getCode())
                .append("quantity", getQuantity())
                .append("amount", getAmount())
                .append("payWay", getPayWay())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
