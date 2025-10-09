package com.ruoyi.web.controller.xkt.vo.quickFunction;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuickFuncUpdateVO {

    @NotNull(message = "用户ID不能为空!")
    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @NotNull(message = "菜单id列表不能为空!")
    @ApiModelProperty(value = "菜单id列表")
    private List<QFMenuVO> menuList;

    @Data
    @ApiModel
    @Valid
    public static class QFMenuVO {
        @NotNull(message = "菜单ID不能为空!")
        @ApiModelProperty(value = "菜单ID")
        private Long menuId;
        @NotNull(message = "排序不能为空!")
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
    }

}
