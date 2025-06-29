package com.ruoyi.web.controller.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author liangyq
 * @date 2025-06-29
 */
@ApiModel
@Data
public class UrlVO {

    @NotEmpty
    @ApiModelProperty("url")
    private String url;

}
