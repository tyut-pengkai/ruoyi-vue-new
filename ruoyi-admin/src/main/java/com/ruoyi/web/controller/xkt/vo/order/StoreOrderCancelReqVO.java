package com.ruoyi.web.controller.xkt.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liangyq
 * @date 2025-04-07 18:16
 */
@ApiModel
@Data
public class StoreOrderCancelReqVO {

    @NotNull(message = "订单ID不能为空")
    @ApiModelProperty(value = "订单ID")
    private Long storeOrderId;
}
