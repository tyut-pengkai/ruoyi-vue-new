package com.ruoyi.web.controller.xkt.vo.storeProdDetail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品当前颜色")
@Data
public class StoreProdDetailVO {

    @NotBlank(message = "详情内容不能为空!")
    @ApiModelProperty(name = "详情内容")
    private String detail;

}
