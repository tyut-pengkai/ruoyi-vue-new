package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 档口打印条形码记录对象 store_product_barcode_record
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreProductBarcodeRecord extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品条码打印记录ID
     */
    @TableId
    private Long id;

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
     * 打印的条形码
     */
    @Excel(name = "打印的条形码")
    private String barcode;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeProdId", getStoreProdId())
                .append("storeProdColorSizeId", getStoreProdColorSizeId())
                .append("barcode", getBarcode())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
