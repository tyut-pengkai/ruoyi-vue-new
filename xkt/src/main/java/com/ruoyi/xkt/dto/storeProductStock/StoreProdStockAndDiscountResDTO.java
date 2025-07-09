package com.ruoyi.xkt.dto.storeProductStock;

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
@ApiModel("档口商品库存详情数据")
@Data
@Accessors(chain = true)
public class StoreProdStockAndDiscountResDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口客户ID")
    private Long storeCusId;
    @ApiModelProperty(value = "档口客户名称")
    private String storeCusName;
    @ApiModelProperty(value = "销售金额")
    private BigDecimal price;
    @ApiModelProperty(value = "优惠金额")
    private Integer discount;
    @ApiModelProperty(value = "大小码加价")
    private Integer overPrice;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "档口商品颜色ID")
    private Long storeProdColorId;
    @ApiModelProperty(value = "档口颜色ID")
    private Long storeColorId;
    @ApiModelProperty(value = "颜色名称")
    private String colorName;
    @ApiModelProperty(value = "尺码库存列表")
    private List<SPSADSizeDTO> sizeStockList;

    @Data
    public static class SPSADSizeDTO {
        @ApiModelProperty(value = "尺码")
        private Integer size;
        @ApiModelProperty(value = "库存")
        private Integer stock;
    }

}
