package com.ruoyi.web.controller.xkt.vo.advertRound;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.xkt.dto.advertRound.AdRoundStoreResDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("当前类型档口营销推广数据")
@Data
@Accessors(chain = true)
public class AdRoundStoreResVO {

    @ApiModelProperty(value = "时间范围的广告轮次列表")
    private List<ADRSRoundTimeRangeVO> timeRangeList;
    @ApiModelProperty(value = "位置枚举的广告轮次列表")
    private List<ADRSRoundPositionVO> positionList;
    @ApiModelProperty(value = "已订购的推广轮次记录")
    private List<ADRSRoundRecordVO> boughtRoundList;

    @Data
    @Accessors(chain = true)
    @ApiModel(value = "类型为时间范围的广告轮次")
    public static class ADRSRoundTimeRangeVO {
        @ApiModelProperty(value = "广告ID")
        private Long advertId;
        @ApiModelProperty(value = "广告轮次ID")
        private Integer roundId;
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
        @ApiModelProperty(value = "竞价状态")
        private Integer biddingStatus;
        @ApiModelProperty(value = "竞价状态名称")
        private String biddingStatusName;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @ApiModel(value = "类型为位置枚举的广告轮次")
    public static class ADRSRoundPositionVO {
        @ApiModelProperty(value = "广告ID")
        private Long advertId;
        @ApiModelProperty(value = "广告轮次ID")
        private Long roundId;
        @ApiModelProperty(value = "typeId")
        private Integer typeId;
        @ApiModelProperty(value = "广告位置 A B C D E")
        private String position;
//        @ApiModelProperty(value = "推广档口ID")
//        private Long storeId;
        @ApiModelProperty(value = "对象锁符号")
        private String symbol;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @ApiModel(value = "已抢购的推广记录")
    public static class ADRSRoundRecordVO {
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
        @ApiModelProperty(value = "对象锁符号")
        private String symbol;
        @ApiModelProperty(value = "广告位置 A B C D E")
        private String position;
        @ApiModelProperty(value = "竞价状态")
        private Integer biddingStatus;
        @ApiModelProperty(value = "竞价状态名称及描述")
        private String biddingStatusName;
    }


}
