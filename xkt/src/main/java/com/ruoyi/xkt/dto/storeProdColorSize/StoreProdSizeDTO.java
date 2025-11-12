package com.ruoyi.xkt.dto.storeProdColorSize;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
public class StoreProdSizeDTO {

    @ApiModelProperty(value = "档口商品颜色ID")
    private Long storeColorId;
    @ApiModelProperty(value = "颜色名称")
    private String colorName;
    @ApiModelProperty(value = "内里材质")
    private String shoeUpperLiningMaterial;
    @ApiModelProperty(value = "商品尺码")
    private Integer size;
    @ApiModelProperty(value = "是否是标准尺码")
    private Integer standard;
    @ApiModelProperty(value = "尺码价格")
    private BigDecimal price;

}
