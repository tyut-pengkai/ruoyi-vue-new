package com.ruoyi.xkt.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 档口商品颜色的尺码对象 store_product_color_size
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreProductColorSize extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品颜色尺码ID
     */
    private Long storeProdColorSizeId;

    /**
     * 档口商品颜色ID
     */
    @Excel(name = "档口商品颜色ID")
    private Long storeProdColorId;

    /**
     * 档口商品ID
     */
    @Excel(name = "档口商品ID")
    private Long storeProdId;

    /**
     * 商品尺码
     */
    @Excel(name = "商品尺码")
    private Integer size;

    /**
     * 档口商品颜色尺码的前缀
     */
    @Excel(name = "档口商品颜色尺码的前缀")
    private String barcodePrefix;

    /**
     * 是否是标准尺码（0不是 1是）
     */
    @Excel(name = "是否是标准尺码", readConverterExp = "0=不是,1=是")
    private String standard;

    /**
     * 版本号
     */
    @Excel(name = "版本号")
    private Long version;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("storeProdColorSizeId", getStoreProdColorSizeId())
                .append("storeProdColorId", getStoreProdColorId())
                .append("storeProdId", getStoreProdId())
                .append("size", getSize())
                .append("barcodePrefix", getBarcodePrefix())
                .append("standard", getStandard())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
