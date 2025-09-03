package com.ruoyi.web.controller.xkt.vo.userShoppingCart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("进货车删除")
@Data

public class ShopCartDeleteVO {

    @NotNull(message = "进货车ID列表不可为空!")
    @ApiModelProperty(value = "进货车ID列表", required = true)
    private List<Long> shoppingCartIdList;

}
