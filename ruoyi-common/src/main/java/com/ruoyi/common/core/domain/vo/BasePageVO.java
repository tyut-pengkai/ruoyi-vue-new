package com.ruoyi.common.core.domain.vo;

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
    @ApiModelProperty(value = "pageNum", required = true)
    private int pageNum;
    @NotNull(message = "pageSize不能为空")
    @ApiModelProperty(value = "pageSize", required = true)
    private int pageSize;

}
