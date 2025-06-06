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
public class PasswordChangeVO {

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @NotEmpty(message = "手机号不能为空")
    @ApiModelProperty("手机号")
    private String phoneNumber;

    @NotEmpty(message = "短信验证码不能为空")
    @ApiModelProperty("短信验证码")
    private String code;

    @NotEmpty(message = "新密码不能为空")
    @ApiModelProperty("新密码")
    private String newPassword;
}
