package com.ruoyi.web.controller.xkt.vo.quickFunction;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口快捷功能")
@Data
@Builder

@NoArgsConstructor
@AllArgsConstructor
public class StoreQuickFuncVO {

    @ApiModelProperty(value = "菜单名称")
    private String menuName;
    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;
    @ApiModelProperty(value = "路由地址")
    private String path;
    @ApiModelProperty(value = "组件路径")
    private String component;
    @ApiModelProperty(value = "菜单图标")
    private String icon;

}
