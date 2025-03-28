package com.ruoyi.web.controller.xkt.vo.storeProdColorSize;

import com.ruoyi.common.annotation.Excel;
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
@ApiModel("档口商品当前尺码")
@Data
public class StoreProdColorSizeVO {

    @NotNull(message = "档口颜色ID不能为空!")
    @ApiModelProperty(name = "档口颜色ID")
    private Long storeColorId;
    @ApiModelProperty(name = "商品尺码")
    @NotNull(message = "档口商品定价不能为空!")
    private Integer size;
    @NotBlank(message = "是否是标准尺码不能为空!")
    @ApiModelProperty(name = "是否是标准尺码")
    private String standard;

}
