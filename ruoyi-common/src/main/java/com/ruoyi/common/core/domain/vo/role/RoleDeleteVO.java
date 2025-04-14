package com.ruoyi.common.core.domain.vo.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("系统删除角色")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDeleteVO {

    @NotNull(message = "角色ID列表不能为空")
    @ApiModelProperty(value = "角色ID列表")
    private List<Long> roleIdList;

}
