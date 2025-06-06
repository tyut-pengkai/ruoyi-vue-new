package com.ruoyi.web.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author liangyq
 * @date 2025-06-05 15:41
 */
@ApiModel
@Data
public class LoginSmsReqVO {

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @NotEmpty(message = "手机号不能为空")
    @ApiModelProperty("手机号")
    private String phoneNumber;

    @ApiModelProperty("图形验证码")
    private String code;

    @ApiModelProperty("图形验证码唯一标识")
    private String uuid;
}
