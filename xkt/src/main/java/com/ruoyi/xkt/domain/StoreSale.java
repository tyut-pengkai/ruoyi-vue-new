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
 * 档口销售出库对象 store_sale
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreSale extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口销售出库ID
     */
    @TableId
    private Long id;
    /**
     * 档口ID
     */
    @Excel(name = "档口ID")
    private Long storeId;
    /**
     * 档口客户ID
     */
    @Excel(name = "档口客户ID")
    private Long storeCusId;
    /**
     * 档口客户名称呢
     */
    private String storeCusName;
    /**
     * 销售类型（普通销售、销售退货、普通销售/销售退货）GENERAL_SALE SALE_REFUND SALE_AND_REFUND
     */
    @Excel(name = "销售类型")
    private String saleType;
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
     * 支付方式（支付宝、微信、现金、欠款）
     */
    @Excel(name = "支付方式", readConverterExp = "支=付宝、微信、现金、欠款")
    private String payWay;
    /**
     * 结款状态（已结清、欠款） SETTLED、DEBT
     */
    private String paymentStatus;
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
                .append("storeId", getStoreId())
                .append("storeCusId", getStoreCusId())
                .append("saleType", getSaleType())
                .append("code", getCode())
                .append("voucherDate", getVoucherDate())
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
