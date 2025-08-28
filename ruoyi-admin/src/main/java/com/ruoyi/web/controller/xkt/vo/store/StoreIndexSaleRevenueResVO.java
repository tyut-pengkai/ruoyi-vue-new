package com.ruoyi.web.controller.xkt.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口首页销售额")
@Data
public class StoreIndexSaleRevenueResVO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "销售出库金额")
    private BigDecimal saleAmount;
    @ApiModelProperty(value = "单据日期")
    private Integer day;

}
