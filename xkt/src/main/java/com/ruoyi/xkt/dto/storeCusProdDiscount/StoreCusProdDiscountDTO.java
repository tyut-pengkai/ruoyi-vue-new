package com.ruoyi.xkt.dto.storeCusProdDiscount;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口客户优惠")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreCusProdDiscountDTO {

    @ApiModelProperty(name = "档口ID")
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @ApiModelProperty(name = "档口客户ID")
    private Long storeCusId;
    @ApiModelProperty(name = "客户名称")
    private String storeCusName;
    @ApiModelProperty(name = "客户联系电话")
    private String phone;
    @ApiModelProperty(name = "优惠金额")
    private BigDecimal discount;

}
