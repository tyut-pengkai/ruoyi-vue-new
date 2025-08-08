package com.ruoyi.web.controller.system.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author liangyq
 * @date 2025-06-04 16:18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserLoginInfoVO extends UserInfoVO {

    @ApiModelProperty("当前角色")
    private RoleInfoVO currentRole;

    @ApiModelProperty("当前菜单ID")
    private List<Long> currentMenuIds;

    @ApiModelProperty("当前菜单树")
    private List<MenuTreeNodeVO> currentMenuTreeNodes;

    @ApiModelProperty("当前档口ID")
    private Long currentStoreId;

    @ApiModelProperty("当前档口名称")
    private String currentStoreName;

    @ApiModelProperty("当前档口状态[1:待审核 2:审核驳回 3:试用期 4:正式使用 5:强制清退 null:未认证]")
    private Integer currentStoreStatus;

}
