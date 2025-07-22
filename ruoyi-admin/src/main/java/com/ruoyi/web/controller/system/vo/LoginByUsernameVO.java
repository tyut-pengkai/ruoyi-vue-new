package com.ruoyi.web.controller.system.vo;

import com.ruoyi.web.controller.xkt.vo.AliCaptchaAuthReqVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

/**
 * @author liangyq
 * @date 2025-06-05 15:41
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LoginByUsernameVO extends AliCaptchaAuthReqVO {
    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空")
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    /**
     * 用户密码
     */
    @NotEmpty(message = "用户密码不能为空")
    @ApiModelProperty(value = "用户密码", required = true)
    private String password;
}
