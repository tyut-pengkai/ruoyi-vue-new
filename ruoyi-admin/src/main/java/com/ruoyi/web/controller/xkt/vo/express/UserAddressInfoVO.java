package com.ruoyi.web.controller.xkt.vo.express;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liangyq
 * @date 2025-05-11 23:19
 */
@ApiModel
@Data
public class UserAddressInfoVO {

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "收件人名称")
    private String receiveName;

    @ApiModelProperty(value = "收件人电话")
    private String receivePhone;

    @ApiModelProperty(value = "省编码")
    private String provinceCode;

    @ApiModelProperty(value = "省名称")
    private String provinceName;

    @ApiModelProperty(value = "市名称")
    private String cityCode;

    @ApiModelProperty(value = "市编码")
    private String cityName;

    @ApiModelProperty(value = "区县编码")
    private String countyCode;

    @ApiModelProperty(value = "区县名称")
    private String countyName;

    @ApiModelProperty(value = "详细地址")
    private String detailAddress;
}
