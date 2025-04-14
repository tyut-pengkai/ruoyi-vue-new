package com.ruoyi.common.core.domain.vo.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("系统编辑角色")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleUpdateVO {

    @ApiModelProperty(value = "角色ID 新增不传,编辑必传")
    private Long roleId;
    @NotNull(message = "是否为管理员角色不能为空")
    @ApiModelProperty(value = "是否为管理员角色 ")
    private Boolean admin;
    @NotBlank(message = "角色名称不能为空")
    @Size(min = 0, max = 30, message = "角色名称长度不能超过30个字")
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @NotBlank(message = "权限字符不能为空")
    @Size(min = 0, max = 100, message = "权限字符长度不能超过100个字")
    @ApiModelProperty(value = "角色权限")
    private String roleKey;
    @NotNull(message = "显示顺序不能为空")
    @ApiModelProperty(value = "角色排序")
    private Integer roleSort;
    @ApiModelProperty(value = "角色状态（0正常 1停用）")
    private String status;
    @Size(min = 0, max = 50, message = "权限字符长度不能超过50个字")
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "菜单组")
    @NotNull(message = "菜单组不能为空")
    private List<Long> menuIdList;

}
