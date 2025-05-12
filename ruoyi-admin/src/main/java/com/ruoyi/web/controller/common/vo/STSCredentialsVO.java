package com.ruoyi.web.controller.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author liangyuqi
 * @date 2021/11/15 14:36
 */
@ApiModel
@Getter
@Setter
@ToString
public class STSCredentialsVO {
    /**
     * ID
     */
    @ApiModelProperty("ID")
    private String accessKeyId;
    /**
     * Secret
     */
    @ApiModelProperty("Secret")
    private String accessKeySecret;
    /**
     * token
     */
    @ApiModelProperty("token")
    private String securityToken;
    /**
     * 有效时长
     */
    @ApiModelProperty("凭证的有效时长，单位秒")
    private Long expiredDuration;
    /**
     * regionId
     */
    @ApiModelProperty("regionId")
    private String regionId;
    /**
     * endPoint
     */
    @ApiModelProperty("endPoint")
    private String endPoint;
    /**
     * 桶
     */
    @ApiModelProperty("bucketName")
    private String bucketName;
    /**
     * 公共读桶
     */
    @ApiModelProperty("publicBucketName")
    private String publicBucketName;
    /**
     * https标记
     */
    @ApiModelProperty("https标记")
    private Boolean httpsFlag;
}
