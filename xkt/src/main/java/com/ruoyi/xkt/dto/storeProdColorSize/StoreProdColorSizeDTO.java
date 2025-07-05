package com.ruoyi.xkt.dto.storeProdColorSize;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
public class StoreProdColorSizeDTO {

    @ApiModelProperty(value = "档口商品颜色尺码ID")
    private Long storeProdColorSizeId;
    @ApiModelProperty(value = "档口颜色ID")
    private Long storeColorId;
    @ApiModelProperty(value = "颜色名称")
    private String colorName;
    @ApiModelProperty(value = "商品尺码")
    private Integer size;
    @ApiModelProperty(value = "是否是标准尺码")
    private Integer standard;

}
