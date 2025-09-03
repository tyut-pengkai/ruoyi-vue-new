package com.ruoyi.web.controller.xkt.vo.userFavorite;

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
@ApiModel("电商卖家取消商品收藏")
@Data

public class UserFavBatchDeleteVO {

    @NotNull(message = "档口商品ID列表不能为空!")
    @ApiModelProperty(value = "档口商品ID列表", required = true)
    List<Long> storeProdIdList;


}
