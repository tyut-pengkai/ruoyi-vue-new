package com.ruoyi.web.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author liangyq
 * @date 2025-05-29 14:29
 */
@ApiModel
@Data
public class RoleListItemVO {
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
     * 角色状态（0正常 1停用）
     */
    @ApiModelProperty("角色状态（0正常 1停用）")
    private String status;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 档口名称
     */
    @ApiModelProperty("档口名称")
    private String storeName;

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
}
