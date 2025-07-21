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
public class RoleInfoEditByStoreVO {
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
     * 角色排序
     */
    @NotNull(message = "显示顺序不能为空")
    @ApiModelProperty("角色排序")
    private Integer roleSort;

    /**
     * 菜单集
     */
    @ApiModelProperty("菜单ID集")
    private List<Long> menuIds;

    @ApiModelProperty("备注")
    private String remark;
}
