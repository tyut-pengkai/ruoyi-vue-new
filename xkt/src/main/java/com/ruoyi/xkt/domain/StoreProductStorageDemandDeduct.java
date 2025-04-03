package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
    private Long storeProdStorDetailId;

    /**
     * 档口商品需求ID
     */
    @Excel(name = "档口商品需求ID")
    private Long storeProdDemandId;

    /**
     * 档口商品颜色ID
     */
    @Excel(name = "档口商品颜色ID")
    private Long storeProdColorId;

    /**
     * 档口商品ID
     */
    @Excel(name = "档口商品ID")
    private Long storeProdId;

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
     * 尺码30
     */
    @Excel(name = "尺码30")
    @TableField("size_30")
    private Integer size30;
    /**
     * 尺码31
     */
    @Excel(name = "尺码31")
    @TableField("size_31")
    private Integer size31;
    /**
     * 尺码32
     */
    @Excel(name = "尺码32")
    @TableField("size_32")
    private Integer size32;
    /**
     * 尺码33
     */
    @Excel(name = "尺码33")
    @TableField("size_33")
    private Integer size33;
    /**
     * 尺码34
     */

    @Excel(name = "尺码34")
    @TableField("size_34")
    private Integer size34;
    /**
     * 尺码35
     */
    @Excel(name = "尺码35")
    @TableField("size_35")
    private Integer size35;
    /**
     * 尺码36
     */
    @Excel(name = "尺码36")
    @TableField("size_36")
    private Integer size36;
    /**
     * 尺码37
     */
    @Excel(name = "尺码37")
    @TableField("size_37")
    private Integer size37;
    /**
     * 尺码38
     */
    @Excel(name = "尺码38")
    @TableField("size_38")
    private Integer size38;
    /**
     * 尺码39
     */
    @Excel(name = "尺码39")
    @TableField("size_39")
    private Integer size39;
    /**
     * 尺码40
     */
    @Excel(name = "尺码40")
    @TableField("size_40")
    private Integer size40;
    /**
     * 尺码41
     */
    @Excel(name = "尺码41")
    @TableField("size_41")
    private Integer size41;
    /**
     * 尺码42
     */
    @Excel(name = "尺码42")
    @TableField("size_42")
    private Integer size42;
    /**
     * 尺码43
     */
    @Excel(name = "尺码43")
    @TableField("size_43")
    private Integer size43;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("storeProdStorDetailId", getStoreProdStorDetailId())
                .append("storeProdDemandId", getStoreProdDemandId())
                .append("storeProdColorId", getStoreProdColorId())
                .append("storeProdId", getStoreProdId())
                .append("storageCode", getStorageCode())
                .append("demandCode", getDemandCode())
                .append("size30", getSize30())
                .append("size31", getSize31())
                .append("size32", getSize32())
                .append("size33", getSize33())
                .append("size34", getSize34())
                .append("size35", getSize35())
                .append("size36", getSize36())
                .append("size37", getSize37())
                .append("size38", getSize38())
                .append("size39", getSize39())
                .append("size40", getSize40())
                .append("size41", getSize41())
                .append("size42", getSize42())
                .append("size43", getSize43())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
