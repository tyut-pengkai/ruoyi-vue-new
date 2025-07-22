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
public class AliCaptchaAuthReqVO {

    @NotEmpty(message = "lot_number不能为空")
    @ApiModelProperty("lot_number（图像验证参数）")
    private String lot_number;

    @NotEmpty(message = "captcha_output不能为空")
    @ApiModelProperty("captcha_output（图像验证参数）")
    private String captcha_output;

    @NotEmpty(message = "pass_token不能为空")
    @ApiModelProperty("pass_token（图像验证参数）")
    private String pass_token;

    @NotEmpty(message = "gen_time不能为空")
    @ApiModelProperty("gen_time（图像验证参数）")
    private String gen_time;

}
