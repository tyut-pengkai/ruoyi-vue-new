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
 * 档口客户优惠对象 store_customer_product_discount
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreCustomerProductDiscount extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口客户销售优惠ID
     */
    @TableId
    private Long storeCusProdDiscId;

    /**
     * 档口商品ID
     */
    @Excel(name = "档口商品ID")
    private Long storeProdId;

    /**
     * 档口客户ID
     */
    @Excel(name = "档口客户ID")
    private Long storeCusId;

    /**
     * 档口商品颜色ID
     */
    @Excel(name = "档口商品颜色ID")
    private Long storeProdColorId;

    /**
     * 优惠金额
     */
    @Excel(name = "优惠金额")
    private BigDecimal discount;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("storeCusProdDiscId", getStoreCusProdDiscId())
                .append("storeProdId", getStoreProdId())
                .append("storeCusId", getStoreCusId())
                .append("storeProdColorId", getStoreProdColorId())
                .append("discount", getDiscount())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
