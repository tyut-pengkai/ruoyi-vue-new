package com.ruoyi.web.controller.xkt.vo.storeProd;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品SKU信息")
@Data
public class StoreProdSkuResVO {

    @ApiModelProperty(value = "档口商品名称")
    private Long storeProdId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口商品名称")
    private String prodName;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "颜色列表")
    private List<SPColorVO> colorList;

    @Data
    @ApiModel(value = "档口商品SKU")
    public static class SPColorVO {
        @ApiModelProperty(value = "档口颜色ID")
        private Long storeColorId;
        @ApiModelProperty(value = "颜色名称")
        private String colorName;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "尺码库存列表")
        List<SPSizeStockVO> sizeStockList;
    }


    @Data
    @ApiModel(value = "尺码库存")
    public static class SPSizeStockVO {
        @ApiModelProperty(value = "商品尺码")
        private Integer size;
        @ApiModelProperty(value = "是否是标准尺码")
        private Integer standard;
        @ApiModelProperty(value = "尺码库存")
        private Integer stock;
    }

}
