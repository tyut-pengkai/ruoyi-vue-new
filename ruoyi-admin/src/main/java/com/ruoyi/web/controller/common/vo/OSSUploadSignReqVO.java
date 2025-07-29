package com.ruoyi.web.controller.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author liangyq
 * @date 2025-07-29
 */
@ApiModel
@Data
public class OSSUploadSignReqVO {

    @NotEmpty(message = "accessKeyId不能为空")
    @ApiModelProperty(value = "STS.accessKeyId")
    private String accessKeyId;

    @NotEmpty(message = "accessKeySecret不能为空")
    @ApiModelProperty(value = "STS.accessKeySecret")
    private String accessKeySecret;

    @NotEmpty(message = "securityToken不能为空")
    @ApiModelProperty(value = "STS.securityToken")
    private String securityToken;

    @NotEmpty(message = "桶不能为空")
    @ApiModelProperty(value = "bucket")
    private String bucket;

    @ApiModelProperty(value = "上传到指定文件夹")
    private String uploadDir;

}
