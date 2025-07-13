package com.ruoyi.xkt.dto.storeProdColorSize;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)
public class StoreSaleSnResDTO {

    @ApiModelProperty(value = "是否成功")
    private Boolean success;
    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "档口商品颜色尺码ID")
    private Long storeProdColorId;
    @ApiModelProperty(value = "颜色")
    private String colorName;
    @ApiModelProperty(value = "尺码")
    private Integer size;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "销售条码")
    private String sn;
    @ApiModelProperty(value = "销售单价")
    private BigDecimal price;
    @ApiModelProperty(value = "档口客户优惠金额")
    private BigDecimal discount;
    @ApiModelProperty(value = "大小码加价")
    private BigDecimal overPrice;
    @ApiModelProperty(value = "标准尺码")
    private Integer standard;
    @ApiModelProperty(value = "[退货扫码时才有]优惠后销售单价")
    private BigDecimal discountedPrice;
    @ApiModelProperty(value = "[退货扫码时才有]销售金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "[退货扫码时才有]销售数量")
    private BigDecimal quantity;
    @ApiModelProperty(value = "[退货扫码时才有]其它优惠")
    private BigDecimal otherDiscount;

}
