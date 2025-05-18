package com.ruoyi.web.controller.xkt.vo.storeProdStock;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品颜色的库存及客户的优惠信息")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreProdStockAndDiscountVO {

    @NotNull(message = "档口商品颜色ID不能为空")
    @ApiModelProperty(value = "档口商品颜色ID", required = true)
    private Long storeProdColorId;
    @ApiModelProperty(value = "档口客户ID", required = true)
    @NotNull(message = "档口客户ID不能为空!")
    private Long storeCusId;
    @NotNull(message = "档口ID不能为空")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;

}
