package com.ruoyi.xkt.dto.storeProdColor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品当前颜色")
@Data
public class StoreProdColorResDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口颜色ID")
    private Long storeColorId;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "商品分类名称")
    private String prodCateName;
    @ApiModelProperty(value = "颜色名称")
    private String colorName;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;

}
