package com.ruoyi.xkt.dto.advertRound;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("当前类型档口营销推广数据")
@Data
@Builder
@Accessors(chain = true)
public class AdRoundStoreResDTO {

    @ApiModelProperty(value = "时间范围的广告轮次列表")
    private List<ADRSRoundTimeRangeDTO> timeRangeList;
    @ApiModelProperty(value = "位置枚举的广告轮次列表")
    private List<ADRSRoundPositionDTO> positionList;
    @ApiModelProperty(value = "已订购的推广轮次记录")
    private List<ADRSRoundRecordDTO> recordList;

    @Data
    @Builder
    @Accessors(chain = true)
    @ApiModel(value = "类型为时间范围的广告轮次")
    public static class ADRSRoundTimeRangeDTO {
        @ApiModelProperty(value = "广告ID")
        private Long advertId;
        @ApiModelProperty(value = "广告轮次ID")
        private Integer roundId;
        @ApiModelProperty(value = "typeId")
        private Integer typeId;
        @ApiModelProperty(value = "推广档口ID")
        private Long storeId;
        @ApiModelProperty(value = "对象锁符号")
        private String symbol;
        @ApiModelProperty(value = "竞价状态")
        private Integer biddingStatus;
        @ApiModelProperty(value = "竞价结果描述")
        private String biddingStatusStr;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @ApiModel(value = "类型为位置枚举的广告轮次")
    public static class ADRSRoundPositionDTO {
        @ApiModelProperty(value = "广告ID")
        private Long advertId;
        @ApiModelProperty(value = "广告轮次ID")
        private Long roundId;
        @ApiModelProperty(value = "typeId")
        private Integer typeId;
        @ApiModelProperty(value = "广告位置 A B C D E")
        private String position;
        @ApiModelProperty(value = "推广档口ID")
        private Long storeId;
        @ApiModelProperty(value = "对象锁符号")
        private String symbol;
    }

    @Data
    @Builder
    @Accessors(chain = true)
    @ApiModel(value = "已抢购的推广记录")
    public static class ADRSRoundRecordDTO {
        @ApiModelProperty(value = "广告ID")
        private Long advertId;
        @ApiModelProperty(value = "广告轮次ID")
        private Long roundId;
        @ApiModelProperty(value = "广告类型ID")
        private Integer typeId;
        @ApiModelProperty(value = "推广档口ID")
        private Long storeId;
        @ApiModelProperty(value = "对象锁符号")
        private String symbol;
        @ApiModelProperty(value = "广告类型名称")
        private String typeName;
        @ApiModelProperty(value = "广告位置 A B C D E")
        private String position;
        @ApiModelProperty(value = "竞价结果描述")
        private String biddingStr;
    }


}
