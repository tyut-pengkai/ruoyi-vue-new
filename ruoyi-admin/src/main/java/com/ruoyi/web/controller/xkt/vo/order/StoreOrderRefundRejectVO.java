package com.ruoyi.web.controller.xkt.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-18 16:17
 */
@ApiModel
@Data
public class StoreOrderRefundRejectVO {

    @NotNull(message = "订单ID不能为空")
    @ApiModelProperty(value = "订单ID", required = true)
    private Long storeOrderId;

    @NotEmpty(message = "明细ID不能为空")
    @ApiModelProperty(value = "明细ID集合", required = true)
    private List<Long> storeOrderDetailIds;

    @ApiModelProperty(value = "原因")
    private String refundRejectReason;

}
