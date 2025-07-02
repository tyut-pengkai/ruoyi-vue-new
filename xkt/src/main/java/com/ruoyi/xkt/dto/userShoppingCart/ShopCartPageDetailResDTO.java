package com.ruoyi.xkt.dto.userShoppingCart;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopCartPageDetailResDTO {

    @ApiModelProperty(value = "进货车明细ID")
    private Long shoppingCartDetailId;
    @ApiModelProperty(value = "进货单ID")
    private Long shoppingCartId;
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
    @ApiModelProperty(value = "标注尺码")
    private String standardSize;
    @ApiModelProperty(value = "颜色价格")
    private BigDecimal price;
    @ApiModelProperty(value = "总金额")
    private BigDecimal amount;


}
