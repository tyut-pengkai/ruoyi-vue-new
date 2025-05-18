package com.ruoyi.xkt.dto.storeProductStock;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品颜色的库存及客户的优惠信息")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreProdStockAndDiscountDTO {

    @ApiModelProperty(value = "档口商品颜色ID")
    private Long storeProdColorId;
    @ApiModelProperty(value = "档口客户ID")
    private Long storeCusId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;

}
