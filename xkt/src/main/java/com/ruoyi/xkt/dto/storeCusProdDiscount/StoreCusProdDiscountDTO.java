package com.ruoyi.xkt.dto.storeCusProdDiscount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口客户优惠")
@Data

public class StoreCusProdDiscountDTO {

    @ApiModelProperty(value = "档口ID")
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @ApiModelProperty(value = "档口客户ID")
    private Long storeCusId;
    @ApiModelProperty(value = "客户名称")
    private String storeCusName;
    @ApiModelProperty(value = "客户联系电话")
    private String phone;
    @ApiModelProperty(value = "所有商品优惠金额")
    private Integer allProductDiscount;
    @ApiModelProperty(value = "大小码加价 0 不加 1加价")
    private Integer addOverPrice;

}
