package com.ruoyi.web.controller.xkt.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-18 14:36
 */
@ApiModel
@Data
public class StoreOrderShipByStoreReqVO {

    @NotNull(message = "订单ID不能为空")
    @ApiModelProperty(value = "订单ID", required = true)
    private Long storeOrderId;

    @NotEmpty(message = "订单明细ID不能为空")
    @ApiModelProperty(value = "订单明细ID", required = true)
    private List<Long> storeOrderDetailIds;

    @NotNull(message = "物流ID不能为空")
    @ApiModelProperty(value = "物流ID", required = true)
    private Long expressId;

    @NotEmpty(message = "物流单号不能为空")
    @ApiModelProperty(value = "物流单号", required = true)
    private String expressWaybillNo;

}
