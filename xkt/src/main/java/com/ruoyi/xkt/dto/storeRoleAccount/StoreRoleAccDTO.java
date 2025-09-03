package com.ruoyi.xkt.dto.storeRoleAccount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口子账号")
@Data

public class StoreRoleAccDTO {

    @ApiModelProperty(value = "档口子账号ID，新增为空 编辑必传")
    private Long storeRoleAccId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "子账户名称")
    private String accountName;
    @ApiModelProperty(value = "档口子角色ID")
    private Long storeRoleId;
    @ApiModelProperty(value = "用户ID，用户已注册时传，未注册则不传")
    private Long userId;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "密码")
    private String password;

}
