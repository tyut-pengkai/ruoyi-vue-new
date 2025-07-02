package com.ruoyi.xkt.dto.adminAdvertRound;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("管理员审核档口推广图")
@Data
public class AdminAdRoundAuditDTO {

    @ApiModelProperty(value = "推广轮次ID")
    private Long advertRoundId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "图片审核状态 1 待审核 2 审核通过  3 审核驳回")
    private Integer picAuditStatus;
    @ApiModelProperty(value = "驳回原因")
    private String rejectReason;

}
