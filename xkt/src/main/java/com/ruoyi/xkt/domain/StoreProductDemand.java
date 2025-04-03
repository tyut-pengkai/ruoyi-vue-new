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
 * 档口商品需求单对象 store_product_demand
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreProductDemand extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品需求ID
     */
    @TableId
    private Long id;

    /**
     * 档口ID
     */
    private Long storeId;

    /**
     * 档口工厂ID
     */
    private Long storeFactoryId;

    /**
     * 档口商品需求code
     */
    @Excel(name = "档口商品需求code")
    private String code;

    /**
     * 需求状态 1 待生产 2 生产中 3 生产完成
     */
    @Excel(name = "需求状态")
    private Integer demandStatus;

    /**
     * 备注
     */
    private String remark;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
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
