package com.ruoyi.xkt.dto.advert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("推广营销 上线/下线")
@Data
@Accessors(chain = true)
public class AdvertChangeStatusDTO {

    @ApiModelProperty(value = "推广ID")
    private Long advertId;
    @ApiModelProperty(value = "推广状态")
    private Integer status;

}
