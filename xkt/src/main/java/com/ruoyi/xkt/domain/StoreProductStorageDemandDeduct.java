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
 * 档口商品入库抵扣需求对象 store_product_storage_demand_deducte
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class StoreProductStorageDemandDeduct extends XktBaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 档口商品入库抵扣需求ID
     */
    @TableId
    private Long id;

    /**
     * 档口商品入库明细ID
     */
    @Excel(name = "档口商品入库明细ID")
    private Long storeProdStorageDetailId;

    /**
     * 档口商品需求明细ID
     */
    @Excel(name = "档口商品需求明细ID")
    private Long storeProdDemandDetailId;

    /**
     * 档口商品颜色ID
     */
    @Excel(name = "档口商品颜色ID")
    private Long storeProdColorId;

    /**
     * 入库code
     */
    @Excel(name = "入库code")
    private String storageCode;

    /**
     * 需求code
     */
    @Excel(name = "需求code")
    private String demandCode;

    /**
     * 总的数量
     */
    private Integer quantity;

    /**
     * 尺码
     */
    private Integer size;

    /**
     * 备注说明
     */
    private String remark;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeProdStorDetailId", getStoreProdStorageDetailId())
                .append("storeProdDemandId", getStoreProdDemandDetailId())
                .append("storeProdColorId", getStoreProdColorId())
                .append("storageCode", getStorageCode())
                .append("demandCode", getDemandCode())
                .append("size", getSize())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
