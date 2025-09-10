package com.ruoyi.xkt.dto.advertRound;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("推广类型所有轮次")
@Data
@Accessors(chain = true)
public class AdRoundTypeRoundResDTO {

    @ApiModelProperty(value = "广告ID")
    private Long advertId;
    @ApiModelProperty(value = "广告轮次ID")
    private Integer roundId;
    @ApiModelProperty(value = "是否可购买")
    private Boolean canPurchased;
    @ApiModelProperty(value = "展示类型")
    private Integer showType;
    @ApiModelProperty(value = "广告位置 A B C D E")
    private String position;
    @ApiModelProperty(value = "投放开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;
    @ApiModelProperty(value = "投放开始时间星期几")
    private String startWeekDay;
    @ApiModelProperty(value = "投放结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;
    @ApiModelProperty(value = "投放结束时间星期几")
    private String endWeekDay;
    @ApiModelProperty(value = "轮次持续时间")
    private Integer durationDay;
    @ApiModelProperty(value = "起拍价格")
    private BigDecimal startPrice;
    @ApiModelProperty(value = "对象锁符号")
    private String symbol;
    @ApiModelProperty(value = "当前播放状态")
    private Integer launchStatus;
    @ApiModelProperty(value = "竞价状态")
    private Integer biddingStatus;
    @ApiModelProperty(value = "竞价状态名称")
    private String biddingStatusName;
    @ApiModelProperty(value = "购买上传截止时间")
    private String uploadDeadline;

}
