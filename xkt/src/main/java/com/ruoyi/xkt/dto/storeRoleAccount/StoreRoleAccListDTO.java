package com.ruoyi.xkt.dto.storeRoleAccount;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */

@ApiModel("档口子账号分页查询入参")
@Data
public class StoreRoleAccListDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;

}
