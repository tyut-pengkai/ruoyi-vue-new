package com.ruoyi.web.controller.xkt.vo.express;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author liangyq
 * @date 2025-04-16 22:31
 */
@ApiModel
@Data
public class ExpressAddressParseReqVO {

    @NotEmpty
    @ApiModelProperty(value = "地址，包含地址、姓名、电话等")
    private String address;
}
