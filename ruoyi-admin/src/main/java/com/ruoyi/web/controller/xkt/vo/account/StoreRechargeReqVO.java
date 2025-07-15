package com.ruoyi.web.controller.xkt.vo.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-05-09
 */
@ApiModel
@Data
public class StoreRechargeReqVO {

    @NotNull(message = "充值金额不能为空")
    @ApiModelProperty(value = "充值金额", required = true)
    private BigDecimal amount;
    @NotNull(message = "支付渠道不能为空")
    @ApiModelProperty(value = "支付渠道[1:支付宝]", required = true)
    private Integer payChannel;
    @NotNull(message = "支付来源不能为空")
    @ApiModelProperty(value = "支付来源[1:电脑网站 2:手机网站 3:APP]", required = true)
    private Integer payPage;
}
