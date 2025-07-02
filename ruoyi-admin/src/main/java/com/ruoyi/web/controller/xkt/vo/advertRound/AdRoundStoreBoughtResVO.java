package com.ruoyi.web.controller.xkt.vo.advertRound;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口购买的推广列表")
@Data
@Accessors(chain = true)
public class AdRoundStoreBoughtResVO {

    @ApiModelProperty(value = "广告ID")
    private Long advertId;
    @ApiModelProperty(value = "广告轮次ID")
    private Integer roundId;
    @ApiModelProperty(value = "广告类型ID")
    private Integer typeId;
    @ApiModelProperty(value = "广告类型名称")
    private String typeName;
    @ApiModelProperty(value = "推广档口ID")
    private Long storeId;
    @ApiModelProperty(value = "推广展示类型 时间范围 位置枚举")
    private Integer showType;
    @ApiModelProperty(value = "广告位置类型 时间范围 位置枚举")
    private Integer displayType;
    @ApiModelProperty(value = "投放开始时间")
    @JsonFormat(pattern = "MM月dd日", timezone = "GMT+8")
    private Date startTime;
    @ApiModelProperty(value = "投放结束时间")
    @JsonFormat(pattern = "MM月dd日", timezone = "GMT+8")
    private Date endTime;
    @ApiModelProperty(value = "对象锁符号")
    private String symbol;
    @ApiModelProperty(value = "广告位置 A B C D E")
    private String position;
    @ApiModelProperty(value = "竞价状态")
    private Integer biddingStatus;
    @ApiModelProperty(value = "竞价状态名称及描述")
    private String biddingStatusName;

}
