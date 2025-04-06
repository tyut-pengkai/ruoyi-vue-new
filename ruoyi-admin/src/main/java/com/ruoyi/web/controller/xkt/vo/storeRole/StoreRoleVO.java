package com.ruoyi.web.controller.xkt.vo.storeRole;

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
@ApiModel("档口子角色")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreRoleVO {

    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @ApiModelProperty(value = "档口ID，新增为空 编辑必传")
    private Long storeRoleId;
    @ApiModelProperty(value = "角色名称")
    @NotBlank(message = "角色名称不能为空!")
    @Size(min = 1, max = 30, message = "角色名称长度不能超过30个字!")
    private String roleName;
    @ApiModelProperty(value = "档口子角色备注")
    private String remark;
    @ApiModelProperty(value = "档口子角色菜单权限名称列表")
    List<String> menuList;

}
