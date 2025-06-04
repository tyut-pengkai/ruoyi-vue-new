package com.ruoyi.web.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liangyq
 * @date 2025-06-04 16:56
 */
@ApiModel
@Data
public class RoleSelectReqVO {

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("档口ID")
    private Long relStoreId;

}
