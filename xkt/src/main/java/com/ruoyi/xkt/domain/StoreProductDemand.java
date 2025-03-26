package com.ruoyi.xkt.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 档口商品需求单对象 store_product_demand
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreProductDemand extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品需求ID
     */
    private Long storeProdDemandId;

    /**
     * 档口商品需求code
     */
    @Excel(name = "档口商品需求code")
    private String code;

    /**
     * 需求状态
     */
    @Excel(name = "需求状态")
    private Long demandStatus;

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
                .append("storeProdDemandId", getStoreProdDemandId())
                .append("code", getCode())
                .append("demandStatus", getDemandStatus())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
