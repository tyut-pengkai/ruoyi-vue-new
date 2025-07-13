package com.ruoyi.web.controller.xkt.vo.storeRoleAccount;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreRoleAccUpdateStatusVO {

    @ApiModelProperty(value = "档口子账号ID", required = true)
    @NotNull(message = "档口子账号ID不能为空!")
    private Long storeRoleAccId;
    @ApiModelProperty(value = "档口子账号状态，启用传:0，停用传:2", required = true)
    @NotBlank(message = "档口子账号状态不能为空!")
    private String delFlag;

}
