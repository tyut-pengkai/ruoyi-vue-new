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
public class RoleSelectItemVO {

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("档口ID")
    private Long relStoreId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("档口名称")
    private String relStoreName;

    @ApiModelProperty("是否当前选择")
    private Boolean current;
}
