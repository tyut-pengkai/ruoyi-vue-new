package com.ruoyi.xkt.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 档口商品对象 store_product
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class StoreProduct extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品ID
     */
    private Long storeProdId;

    /**
     * 档口商品名称
     */
    @Excel(name = "档口商品名称")
    private String prodName;

    /**
     * 商品分类ID
     */
    @Excel(name = "商品分类ID")
    private Long prodCateId;

    /**
     * 工厂货号
     */
    @Excel(name = "工厂货号")
    private String factoryArtNum;

    /**
     * 商品货号
     */
    @Excel(name = "商品货号")
    private String prodArtNum;

    /**
     * 商品标题
     */
    @Excel(name = "商品标题")
    private String prodTitle;

    /**
     * 商品重量
     */
    @Excel(name = "商品重量")
    private BigDecimal prodWeight;

    /**
     * 生产价格
     */
    @Excel(name = "生产价格")
    private Integer producePrice;

    /**
     * 大小码加价
     */
    @Excel(name = "大小码加价")
    private Integer overPrice;

    /**
     * 发货时效
     */
    @Excel(name = "发货时效")
    private Integer deliveryTime;

    /**
     * 上架方式（立即上架、定时上架）
     */
    @Excel(name = "上架方式", readConverterExp = "立=即上架、定时上架")
    private Long listingWay;

    /**
     * 下一个生成的条形码尾号
     */
    @Excel(name = "下一个生成的条形码尾号")
    private Integer nextBarcodeNum;

    /**
     * 定时发货时间(精确到小时)
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "定时发货时间(精确到小时)", width = 30, dateFormat = "yyyy-MM-dd")
    private Date listingWaySchedule;

    /**
     * 单据日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "单据日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date voucherDate;

    /**
     * 商品状态
     */
    @Excel(name = "商品状态")
    private Long prodStatus;

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
                .append("storeProdId", getStoreProdId())
                .append("prodName", getProdName())
                .append("prodCateId", getProdCateId())
                .append("factoryArtNum", getFactoryArtNum())
                .append("prodArtNum", getProdArtNum())
                .append("prodTitle", getProdTitle())
                .append("prodWeight", getProdWeight())
                .append("producePrice", getProducePrice())
                .append("overPrice", getOverPrice())
                .append("deliveryTime", getDeliveryTime())
                .append("listingWay", getListingWay())
                .append("nextBarcodeNum", getNextBarcodeNum())
                .append("listingWaySchedule", getListingWaySchedule())
                .append("voucherDate", getVoucherDate())
                .append("prodStatus", getProdStatus())
                .append("version", getVersion())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
