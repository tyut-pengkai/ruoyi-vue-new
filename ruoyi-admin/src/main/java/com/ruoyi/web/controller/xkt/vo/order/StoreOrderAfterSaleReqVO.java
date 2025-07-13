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
public class StoreOrderAfterSaleReqVO {

    @NotNull(message = "订单ID不能为空")
    @ApiModelProperty(value = "订单ID", required = true)
    private Long storeOrderId;
    @NotEmpty(message = "订单明细ID不能为空")
    @ApiModelProperty(value = "订单明细ID", required = true)
    private List<Long> storeOrderDetailIds;
    @NotNull(message = "退货原因不能为空")
    @ApiModelProperty(value = "退货原因code", required = true)
    private String refundReasonCode;
    @ApiModelProperty(value = "物流ID")
    private Long expressId;
    @ApiModelProperty(value = "物流运单号")
    private String expressWaybillNo;

}
