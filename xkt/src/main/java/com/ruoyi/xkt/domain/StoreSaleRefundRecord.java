package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 档口销售返单对象 store_sale_refund_record
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreSaleRefundRecord extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口销售出库返单记录ID
     */
    @TableId
    private Long id;

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
     * 档口客户名称呢
     */
    private String storeCusName;
    /**
     * 销售类型（1 销售、2 退货、3 销售/退货）
     */
    @Excel(name = "销售类型")
    private Integer saleType;
    /**
     * 单据编号
     */
    @Excel(name = "单据编号")
    private String code;
    /**
     * 单据日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "单据日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voucherDate;
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
     * 支付方式（1 支付宝、 2 微信、 3 现金、4 欠款）
     */
    private Integer payWay;
    /**
     * 结款状态（1已结清、2欠款）
     */
    private Integer paymentStatus;
    /**
     * 操作人ID
     */
    private Long operatorId;
    /**
     * 操作人名称
     */
    private String operatorName;
    /**
     * 备注
     */
    private String remark;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeSaleId", getStoreSaleId())
                .append("storeId", getStoreId())
                .append("storeCusId", getStoreCusId())
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
