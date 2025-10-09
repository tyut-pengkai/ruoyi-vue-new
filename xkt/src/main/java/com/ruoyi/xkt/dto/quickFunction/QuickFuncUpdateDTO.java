package com.ruoyi.xkt.dto.quickFunction;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class QuickFuncUpdateDTO {

    @ApiModelProperty(value = "用户ID")
    private Long userId;
    @ApiModelProperty(value = "菜单id列表")
    private List<QFMenuVO> menuList;

    @Data
    @ApiModel
    public static class QFMenuVO {
        @ApiModelProperty(value = "菜单ID")
        private Long menuId;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
    }

}
