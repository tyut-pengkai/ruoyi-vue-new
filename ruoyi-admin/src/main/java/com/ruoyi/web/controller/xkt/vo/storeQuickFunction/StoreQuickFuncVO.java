package com.ruoyi.web.controller.xkt.vo.storeQuickFunction;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口快捷功能")
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class StoreQuickFuncVO {

    @ApiModelProperty(name = "档口ID")
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @ApiModelProperty(name = "档口勾选的快捷功能")
    @NotNull(message = "档口勾选的快捷功能不能为空!")
    private List<QuickFuncDetailVO> checkedList;
    @ApiModelProperty(name = "系统所有的二级菜单列表")
    private List<QuickFuncDetailVO> menuList;

    @Data
    @RequiredArgsConstructor
    public static class QuickFuncDetailVO {
        @ApiModelProperty(name = "菜单名称")
        private String menuName;
        @ApiModelProperty(name = "显示顺序")
        private Integer orderNum;
        @ApiModelProperty(name = "路由地址")
        private String path;
        @ApiModelProperty(name = "组件路径")
        private String component;
        @ApiModelProperty(name = "菜单图标")
        private String icon;
    }


}
