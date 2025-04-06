package com.ruoyi.xkt.dto.storeRoleAccount;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口子账号编辑")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreRoleAccUpdateDTO {

    @ApiModelProperty(value = "档口子账号ID")
    private Long storeRoleAccId;
    @ApiModelProperty(value = "子账户名称")
    private String accountName;
    @ApiModelProperty(value = "档口子角色ID")
    private Long storeRoleId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;

}
