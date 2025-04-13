package com.ruoyi.system.domain.dto.menu;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("系统菜单管理")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuDTO {

    @ApiModelProperty(value = "菜单主键, 新增不传 编辑必传")
    private Long menuId;
    @ApiModelProperty(value = "菜单名称")
    private String menuName;
    @ApiModelProperty(value = "父菜单名称")
    private String parentName;
    @ApiModelProperty(value = "父菜单ID")
    private Long parentId;
    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;
    @ApiModelProperty(value = "路由地址")
    private String path;
    @ApiModelProperty(value = "组件路径")
    private String component;
    @ApiModelProperty(value = "路由参数")
    private String query;
    @ApiModelProperty(value = "路由名称，默认和路由地址相同的驼峰格式（注意：因为vue3版本的router会删除名称相同路由，为避免名字的冲突，特殊情况可以自定义）")
    private String routeName;
    @ApiModelProperty(value = "是否为外链（0是 1否）")
    private String isFrame;
    @ApiModelProperty(value = "是否缓存（0缓存 1不缓存）")
    private String isCache;
    @ApiModelProperty(value = "类型（M目录 C菜单 F按钮）")
    private String menuType;
    @ApiModelProperty(value = "显示状态（0显示 1隐藏）")
    private String visible;
    @ApiModelProperty(value = "菜单状态（0正常 1停用）")
    private String status;
    @ApiModelProperty(value = "权限字符串")
    private String perms;
    @ApiModelProperty(value = "菜单图标")
    private String icon;

}
