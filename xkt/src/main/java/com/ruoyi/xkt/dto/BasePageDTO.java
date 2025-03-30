package com.ruoyi.xkt.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("基础分页")
@Data
public class BasePageDTO {

    @ApiModelProperty(name = "pageNum")
    private int pageNum;
    @ApiModelProperty(name = "pageSize")
    private int pageSize;

}
