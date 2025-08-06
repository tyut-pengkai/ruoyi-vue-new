package com.ruoyi.web.controller.xkt.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author liangyq
 * @date 2025-07-22
 */
@ApiModel
@Data
public class TencentCaptchaAuthReqVO {

    @NotEmpty(message = "ticket不能为空")
    @ApiModelProperty("ticket（图像验证参数）")
    private String ticket;

    @NotEmpty(message = "randstr不能为空")
    @ApiModelProperty("randstr（图像验证参数）")
    private String randstr;
}
