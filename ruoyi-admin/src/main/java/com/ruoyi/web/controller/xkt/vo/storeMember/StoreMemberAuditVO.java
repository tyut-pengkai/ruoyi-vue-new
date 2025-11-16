package com.ruoyi.web.controller.xkt.vo.storeMember;

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
@Data
@ApiModel
@Accessors(chain = true)
public class StoreMemberAuditVO {

    @NotNull(message = "档口会员ID不能为空!")
    @ApiModelProperty(value = "档口会员ID", required = true)
    private Long storeMemberId;
    @NotNull(message = "会员状态不能为空!")
    @ApiModelProperty(value = "会员状态", required = true)
    private Integer memberStatus;

}
