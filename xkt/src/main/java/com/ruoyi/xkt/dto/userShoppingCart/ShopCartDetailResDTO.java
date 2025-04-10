package com.ruoyi.xkt.dto.userShoppingCart;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@ApiModel("档口商品详情返回数据")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
public class ShopCartDetailResDTO {

    @ApiModelProperty("档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "档口商品尺码库存列表")
    private List<SCDStoreProdColorDTO> colorList;
    @ApiModelProperty(value = "标准尺码")
    private List<Integer> standardSizeList;
    @ApiModelProperty(value = "进货车明细列表")
    List<SCDDetailDTO> detailList;

    @Data
    @ApiModel(value = "档口商品基本信息")
    @Accessors(chain = true)
    public static class SCDDetailDTO {
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeProdColorId;
        @ApiModelProperty(value = "尺码")
        private Integer size;
        @ApiModelProperty(value = "商品数量")
        private Integer quantity;
    }

    @Data
    @ApiModel(value = "档口商品基本信息")
    @Accessors(chain = true)
    public static class SCDStoreProdColorDTO {
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeProdColorId;
        @ApiModelProperty(value = "档口颜色ID")
        private Long storeColorId;
        @ApiModelProperty(value = "颜色名称")
        private String colorName;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "档口商品定价")
        private BigDecimal price;
        @ApiModelProperty(value = "商品尺码及库存")
        List<SCDStoreProdSizeStockDTO> sizeStockList;
    }

    @Data
    @ApiModel(value = "档口商品尺码及库存")
    @Accessors(chain = true)
    public static class SCDStoreProdSizeStockDTO {
        @ApiModelProperty(value = "商品尺码")
        private Integer size;
        @ApiModelProperty(value = "尺码库存")
        private Integer stock;
    }

}
