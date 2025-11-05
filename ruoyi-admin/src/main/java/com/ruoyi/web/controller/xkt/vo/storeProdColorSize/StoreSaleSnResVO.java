package com.ruoyi.web.controller.xkt.vo.storeProdColorSize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel(value = "档口销售扫码返回数据")

public class StoreSaleSnResVO {

    @ApiModelProperty(value = "是否成功")
    private Boolean success;
    @ApiModelProperty(value = "销售：当前选择的客户名；退货：上一次销售的客户名")
    private String storeCusName;
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
    @ApiModelProperty(value = "标准尺码")
    private Integer standard;
    @ApiModelProperty(value = "[退货扫码时才有]销售时间")
    private String soldTime;
    @ApiModelProperty(value = "优惠后销售单价")
    private BigDecimal discountedPrice;
    @ApiModelProperty(value = "销售金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "销售数量")
    private BigDecimal quantity;

}
