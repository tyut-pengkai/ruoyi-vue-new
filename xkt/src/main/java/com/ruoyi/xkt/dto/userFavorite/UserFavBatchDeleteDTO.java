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

public class UserFavBatchDeleteDTO {

    @ApiModelProperty("档口商品ID列表")
    List<Long> storeProdIdList;

}
