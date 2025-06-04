package com.ruoyi.web.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-29 16:39
 */
@ApiModel
@Data
public class RoleInfoEditVO {
    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    private Long roleId;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字")
    @ApiModelProperty("角色名称")
    private String roleName;

    /**
     * 角色权限
     */
    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字")
    @ApiModelProperty("角色权限")
    private String roleKey;

    /**
     * 角色排序
     */
    @NotNull(message = "显示顺序不能为空")
    @ApiModelProperty("角色排序")
    private Integer roleSort;

    /**
     * 档口ID
     */
    @ApiModelProperty("档口ID")
    private Long storeId;

    /**
     * 菜单集
     */
    @ApiModelProperty("菜单ID集")
    private List<Long> menuIds;
}
