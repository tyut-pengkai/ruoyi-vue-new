package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 档口销售明细对象 store_sale_detail
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreSaleDetail extends XktBaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 档口商品销售出库明细ID
     */
    @TableId
    private Long id;
    /**
     * 档口商品销售ID
     */
    @Excel(name = "档口商品销售ID")
    private Long storeSaleId;
    /**
     * 档口商品ID
     */
    @Excel(name = "档口商品ID")
    private Long storeProdId;
    /**
     * 档口商品颜色ID
     */
    @Excel(name = "档口商品颜色ID")
    private Long storeProdColorId;
    /**
     * 颜色
     */
    private String colorName;
    /**
     * 尺码
     */
    private Integer size;
    /**
     * 销售类型（普通销售、销售退货）
     */
    @Excel(name = "销售类型", readConverterExp = "普=通销售、销售退货")
    private String saleType;
    /**
     * 商品货号
     */
    @Excel(name = "商品货号")
    private String prodArtNum;
    /**
     * 销售单价
     */
    @Excel(name = "销售单价")
    private BigDecimal price;
    /**
     * 给客户优惠后单价
     */
    @Excel(name = "给客户优惠后单价")
    private BigDecimal discountedPrice;
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
     * 其它优惠
     */
    @Excel(name = "其它优惠")
    private BigDecimal otherDiscount;
    /**
     * 条码
     */
    @Excel(name = "条码")
    private String sns;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeSaleId", getStoreSaleId())
                .append("storeProdId", getStoreProdId())
                .append("storeProdColorId", getStoreProdColorId())
                .append("saleType", getSaleType())
                .append("price", getPrice())
                .append("discountedPrice", getDiscountedPrice())
                .append("quantity", getQuantity())
                .append("amount", getAmount())
                .append("otherDiscount", getOtherDiscount())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
