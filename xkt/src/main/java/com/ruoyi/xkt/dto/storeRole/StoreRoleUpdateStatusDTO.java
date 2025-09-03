package com.ruoyi.xkt.dto.storeRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口子角色停用启用")
@Data

public class StoreRoleUpdateStatusDTO {

    @ApiModelProperty(value = "档口子角色ID")
    private Long storeRoleId;
    @ApiModelProperty(value = "档口子角色状态")
    private String delFlag;

}
