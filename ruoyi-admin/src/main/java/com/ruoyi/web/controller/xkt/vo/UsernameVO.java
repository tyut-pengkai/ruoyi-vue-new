package com.ruoyi.web.controller.xkt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author liangyq
 * @date 2025-06-06
 */
@ApiModel
@Data
public class UsernameVO {

    @NotEmpty(message = "用户名不能为空")
    @ApiModelProperty("用户名")
    private String userName;
}
