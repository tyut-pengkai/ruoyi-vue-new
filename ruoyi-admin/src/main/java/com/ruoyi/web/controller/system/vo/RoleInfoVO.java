package com.ruoyi.web.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-28 17:23
 */
@ApiModel
@Data
public class RoleInfoVO {
    /**
     * 角色ID
     */
    @ApiModelProperty("角色ID")
    private Long roleId;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String roleName;

    /**
     * 角色权限
     */
    @ApiModelProperty("角色权限")
    private String roleKey;

    /**
     * 角色排序
     */
    @ApiModelProperty("角色排序")
    private Integer roleSort;

    /**
     * 档口ID
     */
    @ApiModelProperty("档口ID")
    private Long storeId;

    /**
     * 创建者
     */
    @ApiModelProperty("创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新者
     */
    @ApiModelProperty("更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 角色ID，用户角色关联表字段
     */
    @ApiModelProperty("角色ID，用户角色关联表字段")
    private Long relUserId;

    /**
     * 档口ID，用户角色关联表字段
     */
    @ApiModelProperty("档口ID，用户角色关联表字段")
    private Long relStoreId;

    /**
     * 菜单集
     */
    @ApiModelProperty("菜单集")
    private List<MenuInfoVO> menus;

    @ApiModelProperty("菜单ID集")
    private List<Long> menuIds;

}
