package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

/**
 * 档口商品入库对象 store_product_storage
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreProductStorage extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品入库ID
     */
    @TableId
    private Long storeProdStorId;

    /**
     * 入库CODE
     */
    @Excel(name = "入库CODE")
    private String code;

    /**
     * 入库类型
     */
    @Excel(name = "入库类型")
    private Long storageType;

    /**
     * 数量
     */
    @Excel(name = "数量")
    private Integer totalNum;

    /**
     * 生产成本金额
     */
    @Excel(name = "生产成本金额")
    private BigDecimal totalProducePrice;

    /**
     * 入库状态
     */
    @Excel(name = "入库状态")
    private String storageStatus;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("storeProdStorId", getStoreProdStorId())
                .append("code", getCode())
                .append("storageType", getStorageType())
                .append("totalNum", getTotalNum())
                .append("totalProducePrice", getTotalProducePrice())
                .append("storageStatus", getStorageStatus())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
