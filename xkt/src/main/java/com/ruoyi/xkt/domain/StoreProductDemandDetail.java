package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 档口商品需求单明细对象 store_product_demand_detail
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreProductDemandDetail extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品需求明细ID
     */
    @TableId
    private Long storeProdDemaDetailId;

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
     * 尺码30
     */
    @Excel(name = "尺码30")
    private Integer size30;

    /**
     * 尺码31
     */
    @Excel(name = "尺码31")
    private Integer size31;

    /**
     * 尺码32
     */
    @Excel(name = "尺码32")
    private Integer size32;

    /**
     * 尺码33
     */
    @Excel(name = "尺码33")
    private Integer size33;

    /**
     * 尺码34
     */
    @Excel(name = "尺码34")
    private Integer size34;

    /**
     * 尺码35
     */
    @Excel(name = "尺码35")
    private Integer size35;

    /**
     * 尺码36
     */
    @Excel(name = "尺码36")
    private Integer size36;

    /**
     * 尺码37
     */
    @Excel(name = "尺码37")
    private Integer size37;

    /**
     * 尺码38
     */
    @Excel(name = "尺码38")
    private Integer size38;

    /**
     * 尺码39
     */
    @Excel(name = "尺码39")
    private Integer size39;

    /**
     * 尺码40
     */
    @Excel(name = "尺码40")
    private Integer size40;

    /**
     * 尺码41
     */
    @Excel(name = "尺码41")
    private Integer size41;

    /**
     * 尺码42
     */
    @Excel(name = "尺码42")
    private Integer size42;

    /**
     * 尺码43
     */
    @Excel(name = "尺码43")
    private Integer size43;

    /**
     * 是否紧急单（0正常 1紧急）
     */
    @Excel(name = "是否紧急单", readConverterExp = "0=正常,1=紧急")
    private String emergency;

    /**
     * 档口商品需求明细状态
     */
    @Excel(name = "档口商品需求明细状态")
    private String detailStatus;


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("storeProdDemaDetailId", getStoreProdDemaDetailId())
                .append("storeProdDemandId", getStoreProdDemandId())
                .append("storeProdColorId", getStoreProdColorId())
                .append("storeProdId", getStoreProdId())
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
                .append("emergency", getEmergency())
                .append("detailStatus", getDetailStatus())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
