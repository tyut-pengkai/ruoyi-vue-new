package com.ruoyi.xkt.dto.userAuthentication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("代发审核")
@Data
@Accessors(chain = true)
public class UserAuthAuditDTO {

    @ApiModelProperty(value = "代发ID", required = true)
    private Long userAuthId;
    @ApiModelProperty(value = "审核状态", required = true)
    private Boolean approve;
    @ApiModelProperty(value = "拒绝理由")
    private String rejectReason;

}
