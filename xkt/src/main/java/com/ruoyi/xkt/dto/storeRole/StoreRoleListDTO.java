package com.ruoyi.xkt.dto.storeRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */

@ApiModel("档口子角色分页查询入参")
@Data
public class StoreRoleListDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;

}
