package com.ruoyi.xkt.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.XktBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
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
@Accessors(chain = true)
public class StoreProduct extends XktBaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 档口商品ID
     */
    @TableId
    private Long id;

    /**
     * 档口ID
     */
    private Long storeId;

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
    private BigDecimal producePrice;

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
    private Integer listingWay;

    /**
     * 定时发货时间(精确到小时)
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "定时发货时间(精确到小时)", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
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
    private Integer prodStatus;

    /**
     * 推荐数值
     * @return
     */
    private Long recommendWeight;

    /**
     * 销量数值
     * @return
     */
    private Long saleWeight;

    /**
     * 人气数值
     * @return
     */
    private Long popularityWeight;

}
