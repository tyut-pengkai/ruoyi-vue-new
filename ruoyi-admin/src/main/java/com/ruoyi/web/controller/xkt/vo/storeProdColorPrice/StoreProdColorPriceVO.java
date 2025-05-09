package com.ruoyi.web.controller.xkt.vo.storeProdColorPrice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品新增客户定价获取所有的颜色及价格")
@Data
public class StoreProdColorPriceVO {

    @ApiModelProperty(value = "档口商品颜色ID")
    private Long storeProdColorId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口颜色ID")
    private Long storeColorId;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "颜色名称")
    private String colorName;
    @ApiModelProperty(value = "档口商品颜色定价")
    private BigDecimal price;
    @ApiModelProperty(value = "大小码加价")
    private Integer overPrice;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;

}
