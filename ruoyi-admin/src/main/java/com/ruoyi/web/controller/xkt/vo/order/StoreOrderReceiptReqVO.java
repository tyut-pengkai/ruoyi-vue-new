package com.ruoyi.web.controller.xkt.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liangyq
 * @date 2025-04-18 14:36
 */
@ApiModel
@Data
public class StoreOrderReceiptReqVO {

    @NotNull(message = "订单ID不能为空")
    @ApiModelProperty(value = "订单ID")
    private Long storeOrderId;
}
