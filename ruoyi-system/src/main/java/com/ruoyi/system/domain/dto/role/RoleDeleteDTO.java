package com.ruoyi.system.domain.dto.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("系统删除角色")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDeleteDTO {

    @ApiModelProperty(value = "角色ID列表")
    private List<Long> roleIdList;

}
