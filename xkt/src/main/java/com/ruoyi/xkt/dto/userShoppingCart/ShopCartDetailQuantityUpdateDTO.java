package com.ruoyi.xkt.dto.userShoppingCart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel
@Data
public class ShopCartDetailQuantityUpdateDTO {

    @ApiModelProperty(value = "进货车明细ID")
    private Long shoppingCartDetailId;
    @ApiModelProperty(value = "数量")
    private Integer quantity;

}
