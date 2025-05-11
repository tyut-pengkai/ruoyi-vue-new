package com.ruoyi.web.controller.xkt.vo.userShoppingCart;

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
@ApiModel("电商卖家进货车下单")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopCartListVO {

    @NotNull(message = "档口商品ID列表不可为空!")
    @ApiModelProperty(value = "档口商品ID列表")
    private List<Long> storeProdIdList;

}
