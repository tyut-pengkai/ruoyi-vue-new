package com.ruoyi.common.core.domain.vo.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("系统编辑角色")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleUpdateStatusVO {

    @NotNull(message = "角色ID不能为空")
    @ApiModelProperty(value = "角色ID")
    private Long roleId;
    @NotBlank(message = "角色状态不能为空")
    @ApiModelProperty(value = "角色状态（0正常 1停用）")
    private String status;

}
