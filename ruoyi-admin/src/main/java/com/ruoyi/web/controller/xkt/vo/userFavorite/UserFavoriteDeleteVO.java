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
@ApiModel("用户删除收藏商品")
@Data

public class UserFavoriteDeleteVO {

    @NotNull(message = "用户收藏ID列表不能为空!")
    @ApiModelProperty(value = "用户收藏ID列表", required = true)
    private List<Long> userFavoriteIdList;

}
