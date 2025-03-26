package com.ruoyi.xkt.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 档口当前商品颜色对象 store_product_color
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreProductColor extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品颜色ID
     */
    private Long storeProdColorId;

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
     * 颜色名称
     */
    @Excel(name = "颜色名称")
    private String colorName;

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
                .append("storeProdColorId", getStoreProdColorId())
                .append("storeColorId", getStoreColorId())
                .append("storeProdId", getStoreProdId())
                .append("colorName", getColorName())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
