package com.ruoyi.web.controller.xkt.vo.storeProdStock;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)
public class StoreProdStockTakeResVO {

    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "商品主图")
    private String mainPicUrl;
    @ApiModelProperty(value = "颜色列表")
    private List<SPSTColorSizeVO> colorList;

    @Data
    @ApiModel
    @Accessors(chain = true)
    public static class SPSTColorSizeVO {
        @ApiModelProperty(value = "档口商品库存ID")
        private Long storeProdStockId;
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeProdColorId;
        @ApiModelProperty(value = "颜色ID")
        private Long storeColorId;
        @ApiModelProperty(value = "颜色名称")
        private String colorName;
        @ApiModelProperty(value = "尺码库存列表")
        List<SPSTSizeStockVO> sizeStockList;
    }

    @Data
    @ApiModel
    @Accessors(chain = true)
    public static class SPSTSizeStockVO {
        @ApiModelProperty(value = "档口商品颜色尺码ID")
        private Long storeProdColorSizeId;
        @ApiModelProperty(value = "尺码尺码")
        private Integer size;
        @ApiModelProperty(value = "盘点库存")
        private Integer stock;
    }

}
