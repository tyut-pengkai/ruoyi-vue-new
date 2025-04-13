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
@ApiModel("系统菜单返回数据")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuListDTO {

    @ApiModelProperty(value = "菜单名称")
    private String menuName;
    @ApiModelProperty(value = "菜单状态（0正常 1停用）")
    private String status;

}
