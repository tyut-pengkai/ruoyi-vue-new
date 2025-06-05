package com.ruoyi.web.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author liangyq
 * @date 2025-06-05 15:41
 */
@ApiModel
@Data
public class LoginBySmsCodeVO {

    @NotEmpty(message = "手机号不能为空")
    @ApiModelProperty("手机号")
    private String phoneNumber;

    @NotEmpty(message = "验证码不能为空")
    @ApiModelProperty("验证码")
    private String code;

}
