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
public class LoginByUsername0VO {
    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    /**
     * 用户密码
     */
    @NotEmpty(message = "用户密码不能为空")
    @ApiModelProperty(value = "用户密码", required = true)
    private String password;
}
