package com.ruoyi.xkt.dto.storeRoleAccount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口子账号停用启用")
@Data

public class StoreRoleAccUpdateStatusDTO {

    @ApiModelProperty(value = "档口子账号ID")
    @NotNull(message = "档口子账号ID不能为空!")
    private Long storeRoleAccId;
    @ApiModelProperty(value = "档口子账号状态，启用传:0，停用传:2")
    @NotBlank(message = "档口子账号状态不能为空!")
    private String delFlag;

}
