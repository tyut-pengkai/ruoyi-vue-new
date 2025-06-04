package com.ruoyi.web.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author liangyq
 * @date 2025-06-03 21:17
 */
@ApiModel
@Data
public class PwdResetVO {

    @NotNull(message = "ID不能为空")
    @ApiModelProperty("ID")
    private Long id;

    @NotEmpty(message = "新密码不能为空")
    @ApiModelProperty("新密码")
    private String newPwd;
}
