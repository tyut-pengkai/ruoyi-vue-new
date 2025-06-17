package com.ruoyi.xkt.dto.adminAdvertRound;

import com.ruoyi.xkt.dto.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("管理员推广营销列表查询入参")
@Data
public class AdminAdRoundPageDTO extends BasePageDTO {

    @ApiModelProperty(value = "投放状态 1投放中  2待投放")
    private Integer launchStatus;
    @ApiModelProperty(value = "平台ID")
    private Integer platformId;
    @ApiModelProperty(value = "推广类型")
    private Integer typeId;
    @ApiModelProperty(value = "图片审核状态 1 待审核 2 审核通过  3 审核驳回")
    private Integer picAuditStatus;
    @ApiModelProperty(value = "图片设计 1自主设计、2平台设计")
    private Integer picDesignType;
    @ApiModelProperty(value = "系统拦截 0 未拦截 1已拦截")
    private Integer sysIntercept;
    @ApiModelProperty(value = "播放轮次 2第二轮 3第三轮 4第四轮 5第五轮")
    private Integer roundId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;

}
