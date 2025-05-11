package com.ruoyi.web.controller.xkt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liangyq
 * @date 2025-05-11 23:46
 */
@ApiModel
@Data
public class IdVO {

    @NotNull(message = "ID不能为空")
    @ApiModelProperty("ID")
    private Long id;
}
