package com.ruoyi.web.controller.xkt.vo.userFavorite;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class UserFavStatusCountResVO {

    @ApiModelProperty(value = "在售数量")
    private Integer onSaleNum;
    @ApiModelProperty(value = "已失效数量")
    private Integer expiredNum;

}
