package com.ruoyi.xkt.dto.quickFunction;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel
@Data
@Accessors(chain = true)
public class QuickFuncResDTO {

    @ApiModelProperty(value = "菜单id列表")
    private List<QFMenuDTO> menuList;

    @Data
    @ApiModel
    public static class QFMenuDTO {
        @ApiModelProperty(value = "菜单ID")
        private Long menuId;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
    }

}
