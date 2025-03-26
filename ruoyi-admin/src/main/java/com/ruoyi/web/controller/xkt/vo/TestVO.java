package com.ruoyi.web.controller.xkt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangyq
 * @date 2025-03-26 14:46
 */
@ApiModel
@Getter
@Setter
@ToString
public class TestVO {

    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("描述")
    private String description;

}
