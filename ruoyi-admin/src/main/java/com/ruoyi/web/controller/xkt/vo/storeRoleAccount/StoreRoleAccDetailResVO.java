package com.ruoyi.web.controller.xkt.vo.storeRoleAccount;

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
@Data
@ApiModel
@Accessors(chain = true)
public class StoreRoleAccDetailResVO {

    @ApiModelProperty(value = "档口子账号ID")
    private Long storeRoleAccId;
    @ApiModelProperty(value = "子账户名称")
    private String accountName;
    @ApiModelProperty(value = "档口子角色ID")
    private String storeRoleId;
    @ApiModelProperty(value = "档口所有角色")
    private List<StoreRoleVO> roleList;

    @Data
    @ApiModel
    public static class StoreRoleVO {
        @ApiModelProperty(value = "档口子角色ID")
        private Long storeRoleId;
        @ApiModelProperty(value = "档口子角色名称")
        private String storeRoleName;
    }

}
