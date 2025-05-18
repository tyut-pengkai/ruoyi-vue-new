package com.ruoyi.web.controller.xkt.vo.storeSale;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口今日销售数据")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreTodaySaleVO {

    @ApiModelProperty(value = "storeId")
    private Long storeId;
    @ApiModelProperty(value = "销售数量")
    private Integer saleQuantity;
    @ApiModelProperty(value = "销售金额")
    private BigDecimal saleAmount;
    @ApiModelProperty(value = "退货数量")
    private Integer refundQuantity;
    @ApiModelProperty(value = "退货金额")
    private BigDecimal refundAmount;
    @ApiModelProperty(value = "总金额")
    private BigDecimal amount;

}
