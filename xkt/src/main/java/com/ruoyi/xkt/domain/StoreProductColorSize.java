package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
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
@Accessors(chain = true)
public class StoreProductColorSize extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品颜色尺码ID
     */
    @TableId
    private Long storeProdColorSizeId;

    /**
     * 档口颜色ID
     */
    @Excel(name = "档口颜色ID")
    private Long storeColorId;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("storeProdColorSizeId", getStoreProdColorSizeId())
                .append("storeColorId", getStoreColorId())
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
