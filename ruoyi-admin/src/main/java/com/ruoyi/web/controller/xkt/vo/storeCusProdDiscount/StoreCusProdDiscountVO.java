package com.ruoyi.web.controller.xkt.vo.storeCusProdDiscount;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口客户优惠")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreCusProdDiscountVO {

    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @ApiModelProperty(value = "档口客户ID")
    private Long storeCusId;
    @NotBlank(message = "客户名称不能为空!")
    @ApiModelProperty(value = "客户名称", required = true)
    private String storeCusName;
    @ApiModelProperty(value = "客户联系电话")
    private String phone;
    @NotNull(message = "所有商品优惠金额不能为空!")
    @ApiModelProperty(value = "所有商品优惠金额", required = true)
    private Integer allProductDiscount;
    @NotNull(message = "大小码加价不能为空!")
    @ApiModelProperty(value = "大小码加价 0 不加 1加价", required = true)
    private Integer addOverPrice;

}
