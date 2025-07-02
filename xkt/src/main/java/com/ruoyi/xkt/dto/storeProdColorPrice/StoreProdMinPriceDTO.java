package com.ruoyi.xkt.dto.storeProdColorPrice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)
public class StoreProdMinPriceDTO {

    @ApiModelProperty(value = "档口商品颜色ID")
    private Long storeProdId;
    @ApiModelProperty(value = "档口商品最低定价")
    private BigDecimal price;

}
