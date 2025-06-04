package com.ruoyi.web.controller.system.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-05-28 19:36
 */
@ApiModel
@Data
public class MenuQueryVO {
    /**
     * 菜单ID
     */
    @ApiModelProperty("菜单ID")
    private List<Long> menuIds;

    /**
     * 菜单名称
     */
    @ApiModelProperty("菜单名称")
    private String menuName;

    /**
     * 父菜单ID
     */
    @ApiModelProperty("父菜单ID")
    private Long parentId;

    /**
     * 类型（M目录 C菜单 F按钮）
     */
    @ApiModelProperty("类型（M目录 C菜单 F按钮）")
    private String menuType;

    /**
     * 菜单状态（0正常 1停用）
     */
    @ApiModelProperty("菜单状态（0正常 1停用）")
    private String status;
}
