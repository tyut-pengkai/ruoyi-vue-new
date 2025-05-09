package com.ruoyi.xkt.dto.storeProduct;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品的sku信息")
@Data
public class StoreProdSkuDTO {

    @ApiModelProperty(value = "档口商品颜色ID")
    private Long storeProdColorId;
    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "档口商品颜色名称")
    private String colorName;
    @ApiModelProperty(value = "颜色排序")
    private Integer orderNum;
    @ApiModelProperty(value = "尺码")
    private Integer size;
    @ApiModelProperty(value = "是否是标准尺码")
    private Integer standard;
    @ApiModelProperty(value = "档口商品颜色ID")
    private Long storeColorId;
    @ApiModelProperty(value = "档口商品定价")
    private BigDecimal price;
    @ApiModelProperty(value = "档口商品颜色尺码ID")
    private Long storeProdColorSizeId;

}
