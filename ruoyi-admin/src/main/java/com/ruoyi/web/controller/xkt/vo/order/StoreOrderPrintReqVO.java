package com.ruoyi.web.controller.xkt.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-04-18 14:36
 */
@ApiModel
@Data
public class StoreOrderPrintReqVO {

    @NotEmpty(message = "订单明细ID不能为空")
    @ApiModelProperty(value = "订单明细ID", required = true)
    private List<Long> storeOrderDetailIds;
}
