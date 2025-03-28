package com.ruoyi.xkt.dto.storeProdColorPrice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品当前颜色")
@Data
@RequiredArgsConstructor
public class StoreProdColorPriceDTO {

    @ApiModelProperty(name = "档口商品颜色ID")
    private Long storeColorId;
    @ApiModelProperty(name = "档口商品定价")
    private BigDecimal price;

}
