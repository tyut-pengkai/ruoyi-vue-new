package com.ruoyi.xkt.dto.userFavorite;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("电商卖家取消商品收藏")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserFavBatchDeleteDTO {

    @ApiModelProperty("用户收藏ID")
    List<Long> userFavoriteIdList;

}
