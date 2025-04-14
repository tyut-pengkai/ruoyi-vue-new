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
@ApiModel("系统编辑角色")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleUpdateDTO {

    @ApiModelProperty(value = "角色ID 新增不传,编辑必传")
    private Long roleId;
    @ApiModelProperty(value = "是否为管理员角色 ")
    private Boolean admin;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "角色权限")
    private String roleKey;
    @ApiModelProperty(value = "角色排序")
    private Integer roleSort;
    @ApiModelProperty(value = "角色状态（0正常 1停用）")
    private String status;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "菜单组")
    private List<Long> menuIdList;

}
