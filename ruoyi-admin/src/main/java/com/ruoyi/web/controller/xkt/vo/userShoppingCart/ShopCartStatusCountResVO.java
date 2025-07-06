package com.ruoyi.web.controller.xkt.vo.userShoppingCart;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ShopCartStatusCountResVO {

    @ApiModelProperty(value = "在售数量")
    private Integer onSaleNum;
    @ApiModelProperty(value = "已失效数量")
    private Integer expiredNum;

}
