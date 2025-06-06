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
public class LoginByUsernameVO {
    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 用户密码
     */
    @NotEmpty(message = "用户密码不能为空")
    @ApiModelProperty("用户密码")
    private String password;

    /**
     * 验证码
     */
    @ApiModelProperty("图形验证码")
    private String code;

    /**
     * 唯一标识
     */
    @ApiModelProperty("图形验证码唯一标识")
    private String uuid;
}
