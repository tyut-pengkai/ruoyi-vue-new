package com.ruoyi.web.controller.xkt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("基础分页")
@Data
public class BasePageVO {

    @NotNull(message = "pageNum不能为空")
    @ApiModelProperty(name = "pageNum")
    private int pageNum;
    @NotNull(message = "pageSize不能为空")
    @ApiModelProperty(name = "pageSize")
    private int pageSize;

}
