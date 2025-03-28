package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
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
public class StoreProductDemand extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品需求ID
     */
    @TableId
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
    private String demandStatus;


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
