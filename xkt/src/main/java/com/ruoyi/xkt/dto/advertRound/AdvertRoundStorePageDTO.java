package com.ruoyi.xkt.dto.advertRound;

import com.ruoyi.xkt.dto.BasePageDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel("档口已购推广列表入参")
@Data
public class AdvertRoundStorePageDTO extends BasePageDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "平台ID")
    private Long platformId;
    @ApiModelProperty(value = "推广类型")
    private Integer typeId;
    @ApiModelProperty(value = "投放状态")
    private Integer launchStatus;
    @ApiModelProperty(value = "竞价状态 只查看 已出价 或 竞价成功的状态")
    private List<Integer> biddingStatusList;
    @ApiModelProperty(value = "图片是否设置 1 未设置 2已设置")
    private Integer picSetType;
    @ApiModelProperty(value = "图片设计 1自主设计、2平台设计")
    private Integer picDesignType;
    @ApiModelProperty(value = "图片审核状态 1 待审核 2 审核通过  3 审核驳回")
    private Integer picAuditStatus;
    @ApiModelProperty(value = "投放开始时间")
    private Date startTime;
    @ApiModelProperty(value = "投放结束时间")
    private Date endTime;

}
