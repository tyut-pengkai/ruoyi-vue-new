package com.ruoyi.web.controller.xkt.vo.advert;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)
public class AdvertChangeStatusVO {

    @ApiModelProperty(value = "推广ID")
    @NotNull(message = "推广ID不能为空!")
    private Long advertId;
    @ApiModelProperty(value = "推广状态")
    @NotNull(message = "推广状态不能为空!")
    private Integer status;

}
