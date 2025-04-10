package com.ruoyi.web.controller.xkt.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liangyq
 * @date 2025-04-07 18:16
 */
@ApiModel("订单支付入参")
@Data
public class StoreOrderPayReqVO {

    @NotNull(message = "订单ID不能为空")
    @ApiModelProperty(value = "订单ID")
    private Long storeOrderId;

    @NotNull(message = "支付渠道不能为空")
    @ApiModelProperty(value = "支付渠道[1:支付宝]")
    private Integer payChannel;

    @NotNull(message = "支付来源不能为空")
    @ApiModelProperty(value = "支付来源[1:电脑网站 2:手机网站]")
    private Integer payFrom;
}
