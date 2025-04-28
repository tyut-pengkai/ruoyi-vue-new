package com.ruoyi.web.controller.xkt.vo.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author liangyq
 * @date 2025-04-23 14:41
 */
@ApiModel
@Data
public class AlipayStoreBindVO {
    /**
     * 账号
     */
    @NotEmpty(message = "账号不能为空")
    @ApiModelProperty(value = "账号")
    private String accountOwnerNumber;
    /**
     * 姓名
     */
    @NotEmpty(message = "姓名不能为空")
    @ApiModelProperty(value = "姓名")
    private String accountOwnerName;
    /**
     * 手机号
     */
    @NotEmpty(message = "手机号不能为空")
    @ApiModelProperty(value = "手机号")
    private String accountOwnerPhoneNumber;
    /**
     * 验证码
     */
    @NotEmpty(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码")
    private String verifyCode;
}
