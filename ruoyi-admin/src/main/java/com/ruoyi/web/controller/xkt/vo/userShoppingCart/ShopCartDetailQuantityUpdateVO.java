package com.ruoyi.web.controller.xkt.vo.userShoppingCart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel
@Data
public class ShopCartDetailQuantityUpdateVO {

    @NotNull(message = "进货车明细ID不可为空!")
    @ApiModelProperty(value = "进货车明细ID", required = true)
    private Long shoppingCartDetailId;
    @NotNull(message = "数量不可为空!")
    @ApiModelProperty(value = "数量", required = true)
    private Integer quantity;

}
