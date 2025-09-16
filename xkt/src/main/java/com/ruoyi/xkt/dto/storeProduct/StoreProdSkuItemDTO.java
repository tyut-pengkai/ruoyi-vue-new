package com.ruoyi.xkt.dto.storeProduct;

import io.swagger.annotations.ApiModel;
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
@ApiModel("档口商品SKU信息")
@Data
@Accessors(chain = true)
public class StoreProdSkuItemDTO {

    @ApiModelProperty(value = "档口商品颜色ID")
    private Long storeProdColorId;
    @ApiModelProperty(value = "档口颜色ID")
    private Long storeColorId;
    @ApiModelProperty(value = "颜色名称")
    private String colorName;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    @ApiModelProperty(value = "尺码库存列表")
    List<SPSISizeStockDTO> sizeStockList;

    @Data
    @ApiModel(value = "尺码库存")
    @Accessors(chain = true)
    public static class SPSISizeStockDTO {
        @ApiModelProperty(value = "档口商品颜色尺码ID")
        private Long storeProdColorSizeId;
        @ApiModelProperty(value = "商品尺码")
        private Integer size;
        @ApiModelProperty(value = "档口商品定价")
        private BigDecimal price;
        @ApiModelProperty(value = "是否是标准尺码")
        private Integer standard;
        @ApiModelProperty(value = "尺码库存")
        private Integer stock;
    }

}
