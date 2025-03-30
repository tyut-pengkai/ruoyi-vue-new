package com.ruoyi.web.controller.xkt.vo.storeCustomer;

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
@ApiModel("档口客户销售数据情况")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreCusGeneralSaleVO {

    @ApiModelProperty(name = "档口ID")
    private Long storeId;
    @ApiModelProperty(name = "档口客户ID")
    private Long storeCusId;
    @ApiModelProperty(name = "档口客户名称")
    private String cusName;
    @ApiModelProperty(name = "销售多少双")
    private Long saleCount;
    @ApiModelProperty(name = "销售金额")
    private BigDecimal saleAmount;
    @ApiModelProperty(name = "欠款金额")
    private BigDecimal debtAmount;

}
