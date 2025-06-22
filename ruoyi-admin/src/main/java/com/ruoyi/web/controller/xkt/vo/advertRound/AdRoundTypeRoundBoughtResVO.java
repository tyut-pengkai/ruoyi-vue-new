package com.ruoyi.web.controller.xkt.vo.advertRound;

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
@ApiModel("位置枚举的类型购买数据")
@Data
@Accessors(chain = true)
public class AdRoundTypeRoundBoughtResVO {

    @ApiModelProperty(value = "广告ID")
    private Long advertId;
    @ApiModelProperty(value = "广告轮次ID")
    private Integer roundId;
    @ApiModelProperty(value = "typeId")
    private Integer typeId;
    @ApiModelProperty(value = "广告位置 A B C D E")
    private String position;
    @ApiModelProperty(value = "起拍价格")
    private BigDecimal startPrice;
    @ApiModelProperty(value = "推广档口出价")
    private BigDecimal payPrice;
    @ApiModelProperty(value = "投放开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startTime;
    @ApiModelProperty(value = "投放结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endTime;
    @ApiModelProperty(value = "竞价状态")
    private Integer biddingStatus;
    @ApiModelProperty(value = "竞价状态名称")
    private String biddingStatusName;
    @ApiModelProperty(value = "对象锁符号")
    private String symbol;

}
