package com.ruoyi.xkt.dto.storeRole;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@ApiModel("档口子角色")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreRoleDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口ID，新增为空 编辑必传")
    private Long storeRoleId;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "档口子角色备注")
    private String remark;
    @ApiModelProperty(value = "档口子角色菜单权限名称列表")
    List<String> menuList;

}
