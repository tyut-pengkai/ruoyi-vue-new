package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 档口条形码和第三方系统条形码匹配结果对象 store_product_barcode_match
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreProductBarcodeMatch extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口条形码匹配ID
     */
    @TableId
    private Long storeProdBarcodeMatchId;

    /**
     * 档口商品ID
     */
    @Excel(name = "档口商品ID")
    private Long storeProdId;

    /**
     * 档口商品颜色尺码ID
     */
    @Excel(name = "档口商品颜色尺码ID")
    private Long storeProdColorSizeId;

    /**
     * 其它系统条形码前缀
     */
    @Excel(name = "其它系统条形码前缀")
    private String otherSysBarcodePrefix;

    /**
     * 其它系统条形码
     */
    @Excel(name = "其它系统条形码")
    private String otherSysBarcode;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("storeProdBarcodeMatchId", getStoreProdBarcodeMatchId())
                .append("storeProdId", getStoreProdId())
                .append("storeProdColorSizeId", getStoreProdColorSizeId())
                .append("otherSysBarcodePrefix", getOtherSysBarcodePrefix())
                .append("otherSysBarcode", getOtherSysBarcode())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
