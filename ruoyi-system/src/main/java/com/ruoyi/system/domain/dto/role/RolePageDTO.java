package com.ruoyi.system.domain.dto.role;

import com.ruoyi.system.domain.dto.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("角色分页查询入参")
@Data
public class RolePageDTO extends BasePageDTO {

    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "角色权限")
    private String roleKey;
    @ApiModelProperty(value = "角色状态（0正常 1停用）")
    private String status;

}
