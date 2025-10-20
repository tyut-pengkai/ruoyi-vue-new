package com.ruoyi.web.controller.xkt.vo.userAuthentication;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
@Accessors(chain = true)
public class UserAuthAuditVO {

    @ApiModelProperty(value = "代发ID", required = true)
    @NotNull(message = "代发ID不能为空")
    private Long userAuthId;
    @NotNull(message = "审核状态不能为空")
    @ApiModelProperty(value = "审核状态", required = true)
    private Boolean approve;
    @ApiModelProperty(value = "拒绝理由")
    @Size(min = 0, max = 200, message = "拒绝理由不能超过200个字符")
    private String rejectReason;

}
