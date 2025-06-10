package com.ruoyi.web.controller.system.vo;

import com.ruoyi.common.xss.Xss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-27
 */
@ApiModel
@Data
public class UserInfoEditByStoreVO {
    /**
     * 用户账号
     */
    @Xss(message = "用户账号不能包含脚本字符")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过30个字符")
    @ApiModelProperty("用户账号")
    private String userName;

    /**
     * 手机号码
     */
    @Size(min = 0, max = 11, message = "手机号码长度不能超过11个字符")
    @ApiModelProperty("手机号码")
    private String phonenumber;

    @ApiModelProperty("短信验证码")
    private String code;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String password;

    /**
     * 角色ID集
     */
    @ApiModelProperty("角色ID集")
    private List<Long> roleIds;

}
