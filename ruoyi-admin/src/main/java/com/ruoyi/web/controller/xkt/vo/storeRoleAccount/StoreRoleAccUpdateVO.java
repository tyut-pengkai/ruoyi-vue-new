package com.ruoyi.web.controller.xkt.vo.storeRoleAccount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel

public class StoreRoleAccUpdateVO {

    @ApiModelProperty(value = "档口子账号ID", required = true)
    @NotNull(message = "档口子账号ID不能为空!")
    private Long storeRoleAccId;
    @ApiModelProperty(value = "子账户名称")
    @Size(min = 0, max = 30, message = "子账户名称长度不能超过30个字!")
    private String accountName;
    @ApiModelProperty(value = "档口子角色ID", required = true)
    @NotNull(message = "档口子角色ID不能为空!")
    private Long storeRoleId;
    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;

}
