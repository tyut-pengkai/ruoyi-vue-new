package com.ruoyi.web.controller.xkt.vo.quickFunction;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class QuickFuncVO {

    @ApiModelProperty(value = "业务ID 根据roleId确定，可能为store_id、user_id", required = true)
    @NotNull(message = "业务ID不能为空!")
    private Long bizId;
    @ApiModelProperty(value = "角色ID")
    @NotNull(message = "角色ID不能为空!")
    private Long roleId;
    @ApiModelProperty(value = "档口勾选的快捷功能", required = true)
    @NotNull(message = "档口勾选的快捷功能不能为空!")
    private List<QuickFuncDetailVO> checkedList;
    @ApiModelProperty(value = "系统所有的二级菜单列表")
    private List<QuickFuncDetailVO> menuList;

    @Data
    @RequiredArgsConstructor
    public static class QuickFuncDetailVO {
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


}
