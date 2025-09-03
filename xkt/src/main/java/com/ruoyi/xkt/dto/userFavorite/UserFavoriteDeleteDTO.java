package com.ruoyi.xkt.dto.userFavorite;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data

public class UserFavoriteDeleteDTO {

    @ApiModelProperty(value = "用户收藏ID列表")
    private List<Long> userFavoriteIdList;

}
