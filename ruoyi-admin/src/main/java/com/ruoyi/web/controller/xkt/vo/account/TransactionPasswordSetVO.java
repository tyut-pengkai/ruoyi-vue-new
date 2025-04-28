package com.ruoyi.web.controller.xkt.vo.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author liangyq
 * @date 2025-04-23 15:32
 */
@ApiModel
@Data
public class TransactionPasswordSetVO {
    /**
     * 手机号
     */
    @NotEmpty(message = "手机号不能为空")
    @ApiModelProperty(value = "手机号")
    private String phoneNumber;
    /**
     * 验证码
     */
    @NotEmpty(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码")
    private String verifyCode;
    /**
     * 交易密码
     */
    @NotEmpty(message = "交易密码不能为空")
    @ApiModelProperty(value = "交易密码")
    private String transactionPassword;
}
