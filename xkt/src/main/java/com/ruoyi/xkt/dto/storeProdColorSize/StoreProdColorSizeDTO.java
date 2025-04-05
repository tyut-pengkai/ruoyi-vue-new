package com.ruoyi.xkt.dto.storeProdColorSize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品当前尺码")
@Data
public class StoreProdColorSizeDTO {

    @ApiModelProperty(name = "档口商品颜色尺码ID")
    private Long storeProdColorSizeId;
    @ApiModelProperty(name = "档口颜色ID")
    private Long storeColorId;
    @ApiModelProperty(name = "商品尺码")
    private Integer size;
    @ApiModelProperty(name = "是否是标准尺码")
    private Integer standard;

}
