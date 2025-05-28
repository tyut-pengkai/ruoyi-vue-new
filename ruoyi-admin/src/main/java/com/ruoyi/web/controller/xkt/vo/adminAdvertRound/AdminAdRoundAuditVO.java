package com.ruoyi.web.controller.xkt.vo.adminAdvertRound;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("管理员审核档口推广图")
@Data
public class AdminAdRoundAuditVO {

    @NotNull(message = "advertRoundId不能为空")
    @ApiModelProperty(value = "推广轮次ID")
    private Long advertRoundId;
    @NotNull(message = "storeId不能为空")
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "图片审核状态 1 待审核 2 审核通过  3 审核驳回")
    private Integer picAuditStatus;
    @ApiModelProperty(value = "驳回原因")
    private String rejectReason;

}
