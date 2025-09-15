package com.ruoyi.web.controller.xkt.vo.userShoppingCart;

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
@Data
@ApiModel

public class ShopCartEditDetailResVO {

    @ApiModelProperty("档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "档口商品尺码库存列表")
    private List<StoreProdColorVO> colorList;
    @ApiModelProperty(value = "标准尺码")
    private List<Integer> standardSizeList;
    @ApiModelProperty(value = "进货车明细列表")
    List<SCDDetailVO> detailList;

    @Data
    @ApiModel
    @Accessors(chain = true)
    public static class SCDDetailVO {
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeProdColorId;
        @ApiModelProperty(value = "尺码")
        private Integer size;
        @ApiModelProperty(value = "商品数量")
        private Integer quantity;
    }

    @Data
    @ApiModel
    @Accessors(chain = true)
    public static class StoreProdColorVO {
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeProdColorId;
        @ApiModelProperty(value = "档口颜色ID")
        private Long storeColorId;
        @ApiModelProperty(value = "颜色名称")
        private String colorName;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "商品尺码及库存")
        List<StoreProdSizeStockVO> sizeStockList;
    }

    @Data
    @ApiModel
    @Accessors(chain = true)
    public static class StoreProdSizeStockVO {
        @ApiModelProperty(value = "档口商品定价")
        private BigDecimal price;
        @ApiModelProperty(value = "商品尺码")
        private Integer size;
        @ApiModelProperty(value = "尺码库存")
        private Integer stock;
    }

}
