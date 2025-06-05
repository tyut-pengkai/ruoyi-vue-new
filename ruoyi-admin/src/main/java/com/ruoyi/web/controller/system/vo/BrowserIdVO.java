package com.ruoyi.web.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author liangyq
 * @date 2025-06-05 17:00
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrowserIdVO {

    @NotEmpty(message = "浏览器ID不能为空")
    @ApiModelProperty("扫码登录浏览器ID")
    private String browserId;
}
