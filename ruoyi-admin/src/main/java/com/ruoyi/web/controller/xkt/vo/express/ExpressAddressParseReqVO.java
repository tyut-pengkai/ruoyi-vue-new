package com.ruoyi.web.controller.xkt.vo.express;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author liangyq
 * @date 2025-04-16 22:31
 */
@ApiModel
@Data
public class ExpressAddressParseReqVO {

    @NotEmpty
    @ApiModelProperty(value = "地址，包含地址、姓名、电话等", required = true)
    @Size(min = 0, max = 200, message = "地址，包含地址、姓名、电话等长度不能超过200个字符!")
    private String address;

}
