package com.ruoyi.common.core.domain.vo.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("系统菜单管理")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuVo {

    @ApiModelProperty(value = "菜单主键, 新增不传 编辑必传")
    private Long menuId;
    @ApiModelProperty(value = "菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    @Size(min = 1, max = 50, message = "菜单名称长度不能超过50个字")
    private String menuName;
    @NotNull(message = "父菜单ID不能为空")
    @ApiModelProperty(value = "父菜单ID")
    private Long parentId;
    @NotNull(message = "显示顺序不能为空")
    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;
    @ApiModelProperty(value = "路由地址")
    @Size(min = 1, max = 200, message = "路由地址不能超过200个字")
    private String path;
    @ApiModelProperty(value = "组件路径")
    @Size(min = 1, max = 200, message = "组件路径不能超过255个字")
    private String component;
    @ApiModelProperty(value = "路由参数")
    private String query;
    @ApiModelProperty(value = "路由名称，默认和路由地址相同的驼峰格式（注意：因为vue3版本的router会删除名称相同路由，为避免名字的冲突，特殊情况可以自定义）")
    private String routeName;
    @ApiModelProperty(value = "是否为外链（0是 1否）")
    private String isFrame;
    @ApiModelProperty(value = "是否缓存（0缓存 1不缓存）")
    private String isCache;
    @NotBlank(message = "菜单类型不能为空")
    @ApiModelProperty(value = "类型（M目录 C菜单 F按钮）")
    private String menuType;
    @ApiModelProperty(value = "显示状态（0显示 1隐藏）")
    private String visible;
    @NotBlank(message = "菜单状态不能为空")
    @ApiModelProperty(value = "菜单状态（0正常 1停用）")
    private String status;
    @ApiModelProperty(value = "权限字符串")
    @Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
    private String perms;
    @ApiModelProperty(value = "菜单图标")
    private String icon;

}
