package com.ruoyi.web.controller.xkt.vo.storePordColor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品当前颜色")
@Data
public class StoreProdColorResVO {

    @ApiModelProperty(name = "档口ID")
    private Long storeId;
    @ApiModelProperty(name = "档口颜色ID")
    private Long storeColorId;
    @ApiModelProperty(name = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(name = "商品分类名称")
    private String prodCateName;
    @ApiModelProperty(name = "颜色名称")
    private String colorName;
    @ApiModelProperty(name = "排序")
    private Integer orderNum;

}
