package com.ruoyi.web.controller.xkt.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-04-18 14:36
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreOrderShipRespVO {

    @ApiModelProperty(value = "订单明细ID")
    private Long storeOrderDetailId;

    @ApiModelProperty(value = "物流单号")
    private String expressWaybillNo;

}
