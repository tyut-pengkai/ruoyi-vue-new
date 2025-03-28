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
public class StoreProdColorVO {

    @NotNull(message = "档口颜色ID不能为空!")
    @ApiModelProperty(name = "档口颜色ID")
    private Long storeColorId;
    @NotBlank(message = "颜色名称不能为空!")
    @ApiModelProperty(name = "颜色名称")
    private String colorName;
    @NotNull(message = "排序不能为空!")
    @ApiModelProperty(name = "排序")
    private Integer orderNum;

}
