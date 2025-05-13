package com.ruoyi.xkt.dto.userShoppingCart;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopCartDeleteDTO {

    @ApiModelProperty(value = "进货车ID列表")
    private List<Long> shoppingCartIdList;

}
