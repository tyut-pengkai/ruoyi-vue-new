package com.ruoyi.xkt.dto.storeMember;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
@Accessors(chain = true)
public class StoreMemberAuditDTO {

    @ApiModelProperty(value = "档口会员ID")
    private Long storeMemberId;
    @ApiModelProperty(value = "会员状态")
    private Integer memberStatus;

}
