package com.ruoyi.xkt.dto.storeCusProdDiscount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口客户批量减价、批量抹零")
@Data

public class StoreCusProdDiscExistResDTO {

    @ApiModelProperty(value = "档口客户名称")
    private String storeCusName;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "商品颜色名称")
    private String colorName;
    @ApiModelProperty(value = "已有优惠金额")
    private Integer exitDiscount;
    @ApiModelProperty(value = "最新优惠金额")
    private Integer discount;

}
