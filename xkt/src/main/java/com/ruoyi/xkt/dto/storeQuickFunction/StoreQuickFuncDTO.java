package com.ruoyi.xkt.dto.storeQuickFunction;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口快捷功能")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreQuickFuncDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口勾选的快捷功能")
    List<DetailDTO> checkedList;

    @Data
    @Accessors(chain = true)
    public static class DetailDTO {
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
