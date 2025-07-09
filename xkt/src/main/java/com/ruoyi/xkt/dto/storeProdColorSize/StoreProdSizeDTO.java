package com.ruoyi.xkt.dto.storeProdColorSize;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
public class StoreProdSizeDTO {

    @ApiModelProperty(value = "商品尺码")
    private Integer size;
    @ApiModelProperty(value = "是否是标准尺码")
    private Integer standard;

}
