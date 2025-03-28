package com.ruoyi.xkt.dto.storeProdColor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品当前颜色")
@Data
@RequiredArgsConstructor
public class StoreProdColorDTO {

    @ApiModelProperty(name = "档口颜色ID")
    private Long storeColorId;
    @ApiModelProperty(name = "颜色名称")
    private String colorName;
    @ApiModelProperty(name = "排序")
    private Integer orderNum;

}
