package com.ruoyi.web.controller.xkt.vo.storeProd;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liangyq
 * @date 2025-07-15
 */
@ApiModel
@Data
public class PicPackReqVO {
    /**
     * 文件ID
     */
    @NotNull(message = "文件ID不能为空")
    @ApiModelProperty(value = "文件ID")
    private Long fileId;
    /**
     * 图片验证码UUID
     */
    @ApiModelProperty(value = "图片验证码UUID")
    private String uuid;
    /**
     * 图片验证码CODE
     */
    @ApiModelProperty(value = "图片验证码CODE")
    private String code;
}
