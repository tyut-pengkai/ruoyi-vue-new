package com.ruoyi.web.controller.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author liangyq
 * @date 2025-06-29
 */
@ApiModel
@Data
public class HtmlVO {

    @NotEmpty
    @ApiModelProperty("title")
    private String title;

    @NotNull
    @ApiModelProperty("content")
    private String content;

}
