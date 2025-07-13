package com.ruoyi.xkt.dto.userHomepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
@Accessors(chain = true)
public class UserHomepageOverviewDTO {

    @ApiModelProperty(value = "图搜热款数量")
    private Integer searchHotCount;
    @ApiModelProperty(value = "购物车数量")
    private Integer shopCartCount;
    @ApiModelProperty(value = "关注数量")
    private Integer focusCount;
    @ApiModelProperty(value = "收藏数量")
    private Integer favouriteCount;
    @ApiModelProperty(value = "代发订单数量")
    private Integer orderCount;

}
