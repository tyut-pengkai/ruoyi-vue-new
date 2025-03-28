package com.ruoyi.web.controller.xkt.vo.storeColor;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口所有颜色")
@Data
public class StoreColorVO {

    @ApiModelProperty(name = "档口颜色ID")
    private Long storeColorId;
    @ApiModelProperty(name = "颜色名称")
    private String colorName;
    @ApiModelProperty(name = "排序")
    private Integer orderNum;

}
