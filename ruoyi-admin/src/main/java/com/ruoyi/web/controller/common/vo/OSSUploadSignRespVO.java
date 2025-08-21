package com.ruoyi.web.controller.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liangyq
 * @date 2025-07-29
 */
@ApiModel
@Data
public class OSSUploadSignRespVO {

    @ApiModelProperty(value = "version")
    private String version;

    @ApiModelProperty(value = "policy")
    private String policy;

    @ApiModelProperty(value = "x_oss_credential")
    private String x_oss_credential;

    @ApiModelProperty(value = "x_oss_date")
    private String x_oss_date;

    @ApiModelProperty(value = "signature")
    private String signature;

    @ApiModelProperty(value = "security_token")
    private String security_token;

    @ApiModelProperty(value = "dir")
    private String dir;

    @ApiModelProperty(value = "host")
    private String host;

    @ApiModelProperty(value = "callback")
    private String callback;

    @ApiModelProperty(value = "accessKeyId")
    private String accessKeyId;

}
