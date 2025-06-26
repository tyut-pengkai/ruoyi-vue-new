package com.ruoyi.web.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-06-25
 */
@ApiModel
@Data
public class UserRoleBindReqVO {

    @NotNull
    @ApiModelProperty("角色ID")
    private Long roleId;

    @NotEmpty
    @ApiModelProperty("用户ID")
    private List<Long> userIds;
}
