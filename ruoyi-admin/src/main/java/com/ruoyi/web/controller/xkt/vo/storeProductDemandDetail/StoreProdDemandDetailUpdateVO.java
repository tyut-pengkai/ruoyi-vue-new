package com.ruoyi.web.controller.xkt.vo.storeProductDemandDetail;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel
@Data
public class StoreProdDemandDetailUpdateVO {

    @NotNull(message = "商品需求明细ID不能为空")
    @ApiModelProperty(value = "商品需求明细ID", required = true)
    private Long storeProdDemandDetailId;
    @ApiModelProperty(value = "尺码30")
    private Integer size30;
    @ApiModelProperty(value = "尺码31")
    private Integer size31;
    @ApiModelProperty(value = "尺码32")
    private Integer size32;
    @ApiModelProperty(value = "尺码33")
    private Integer size33;
    @ApiModelProperty(value = "尺码34")
    private Integer size34;
    @ApiModelProperty(value = "尺码35")
    private Integer size35;
    @ApiModelProperty(value = "尺码36")
    private Integer size36;
    @ApiModelProperty(value = "尺码37")
    private Integer size37;
    @ApiModelProperty(value = "尺码38")
    private Integer size38;
    @ApiModelProperty(value = "尺码39")
    private Integer size39;
    @ApiModelProperty(value = "尺码40")
    private Integer size40;
    @ApiModelProperty(value = "尺码41")
    private Integer size41;
    @ApiModelProperty(value = "尺码42")
    private Integer size42;
    @ApiModelProperty(value = "尺码43")
    private Integer size43;

}
