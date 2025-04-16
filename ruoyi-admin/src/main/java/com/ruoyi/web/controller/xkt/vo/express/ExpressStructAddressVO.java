package com.ruoyi.web.controller.xkt.vo.express;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liangyq
 * @date 2025-04-16 16:18
 */
@ApiModel
@Data
public class ExpressStructAddressVO {
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String contactName;
    /**
     * 电话
     */
    @ApiModelProperty(value = "电话")
    private String contactPhoneNumber;
    /**
     * 省
     */
    @ApiModelProperty(value = "省")
    private String provinceCode;
    @ApiModelProperty(value = "省")
    private String provinceName;
    /**
     * 市
     */
    @ApiModelProperty(value = "市")
    private String cityCode;
    @ApiModelProperty(value = "市")
    private String cityName;
    /**
     * 区县
     */
    @ApiModelProperty(value = "区县")
    private String countyCode;
    @ApiModelProperty(value = "区县")
    private String countyName;
    /**
     * 详细地址
     */
    @ApiModelProperty(value = "详细地址")
    private String detailAddress;
}
