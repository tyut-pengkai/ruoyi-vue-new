package com.ruoyi.xkt.dto.userIndex;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("电商卖家首页返回数据")
@Data
@Accessors(chain = true)
public class UserOverallResDTO {

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
