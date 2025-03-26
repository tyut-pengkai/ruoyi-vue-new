package com.ruoyi.xkt.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 档口商品类目信息对象 store_product_category_attribute
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreProductCategoryAttribute extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品类目属性ID
     */
    private Long storeProdAttrId;

    /**
     * 档口商品ID
     */
    @Excel(name = "档口商品ID")
    private Long storeProdId;

    /**
     * 系统设置类目ID
     */
    @Excel(name = "系统设置类目ID")
    private Long sysDictType;

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
                .append("storeProdAttrId", getStoreProdAttrId())
                .append("storeProdId", getStoreProdId())
                .append("sysDictType", getSysDictType())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
