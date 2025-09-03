package com.ruoyi.web.controller.xkt.vo.storeRoleAccount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel

public class StoreRoleAccVO {

    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @ApiModelProperty(value = "档口子角色ID", required = true)
    @NotNull(message = "档口子角色ID不能为空!")
    private Long storeRoleId;
    @ApiModelProperty(value = "子账户名称")
    @Size(min = 0, max = 30, message = "子账户名称长度不能超过30个字!")
    private String accountName;
    @ApiModelProperty(value = "用户ID，用户已注册时传，未注册则不传")
    private Long userId;
    @ApiModelProperty(value = "手机号", required = true)
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "联系电话格式不正确，请输入有效的中国大陆手机号")
    private String phone;
    @ApiModelProperty(value = "密码")
    private String password;

}
