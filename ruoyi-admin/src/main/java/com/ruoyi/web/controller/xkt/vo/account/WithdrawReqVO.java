package com.ruoyi.web.controller.xkt.vo.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-04-23 14:41
 */
@ApiModel
@Data
public class WithdrawReqVO {

    @NotNull(message = "金额不能为空")
    @ApiModelProperty(value = "金额", required = true)
    private BigDecimal amount;

    @NotEmpty(message = "支付密码不能为空")
    @ApiModelProperty(value = "支付密码", required = true)
    private String transactionPassword;

}
