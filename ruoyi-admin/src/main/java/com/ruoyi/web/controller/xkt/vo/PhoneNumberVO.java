package com.ruoyi.web.controller.xkt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * @author liangyq
 * @date 2025-06-06
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneNumberVO {

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @NotEmpty(message = "手机号不能为空")
    @ApiModelProperty("手机号")
    private String phoneNumber;
}
