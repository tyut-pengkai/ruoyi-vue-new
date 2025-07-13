package com.ruoyi.web.controller.xkt.vo.userIndex;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
public class UserOverallResVO {

    @ApiModelProperty(value = "图搜热款数量")
    private Long searchHotCount;
    @ApiModelProperty(value = "进货车数量")
    private Long shoppingCartCount;
    @ApiModelProperty(value = "关注档口数量")
    private Long focusStoreCount;
    @ApiModelProperty(value = "收藏商品数量")
    private Long collectProdCount;
    @ApiModelProperty(value = "代发订单数量")
    private Long sendOrderCount;

}
