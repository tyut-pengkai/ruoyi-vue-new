package com.ruoyi.web.controller.xkt.vo.storeRole;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */

@ApiModel("档口子角色分页查询入参")
@Data
public class StoreRoleListVO {

    @NotNull(message = "档口ID不能为空")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;

}
