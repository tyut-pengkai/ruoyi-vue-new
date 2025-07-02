package com.ruoyi.xkt.dto.userShoppingCart;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShoppingCartEditDTO {

    @ApiModelProperty(value = "进货车ID,编辑必传")
    private Long shoppingCartId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "进货车明细列表")
    List<SCDetailDTO> detailList;

    @Data
    public static class SCDetailDTO {
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeProdColorId;
        @ApiModelProperty(value = "尺码")
        private Integer size;
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeColorId;
        @ApiModelProperty(value = "颜色名称")
        private String colorName;
        @ApiModelProperty(value = "商品数量")
        private Integer quantity;
    }

}
