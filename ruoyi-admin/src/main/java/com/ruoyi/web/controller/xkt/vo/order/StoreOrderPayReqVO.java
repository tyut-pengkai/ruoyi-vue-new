package com.ruoyi.web.controller.xkt.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-07 18:16
 */
@ApiModel("订单支付入参")
@Data
public class StoreOrderPayReqVO {

    @ApiModelProperty(value = "订单ID")
    private Long storeOrderId;

    @ApiModelProperty(value = "支付渠道[1:支付宝]")
    private Integer payChannel;

    @ApiModelProperty(value = "支付来源[1:电脑网站 2:手机网站]")
    private Integer payFrom;
}
