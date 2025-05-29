package com.ruoyi.xkt.dto.advertRound;

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
@ApiModel("档口已订购推广返回列表")
@Data
@Accessors(chain = true)
public class AdvertRoundStorePageResDTO {

    @ApiModelProperty(value = "推广轮次ID")
    private Long advertRoundId;
    @ApiModelProperty(value = "档口名称")
    private String storeName;
    @ApiModelProperty(value = "投放平台")
    private Integer platformId;
    @ApiModelProperty(value = "投放平台")
    private String platformName;
    @ApiModelProperty(value = "投放类型")
    private Integer typeId;
    @ApiModelProperty(value = "投放类型")
    private String typeName;
    @ApiModelProperty(value = "投放位置")
    private String position;
    @ApiModelProperty(value = "投放开始时间")
    private String startTime;
    @ApiModelProperty(value = "投放结束时间")
    private String endTime;
    @ApiModelProperty(value = "出价")
    private String payPrice;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @ApiModelProperty(value = "凭证日期")
    private Date voucherDate;
    @ApiModelProperty(value = "投放状态")
    private Integer launchStatus;
    @ApiModelProperty(value = "投放状态")
    private String launchStatusName;
    @ApiModelProperty(value = "竞价状态")
    private Integer biddingStatus;
    @ApiModelProperty(value = "竞价状态")
    private String biddingStatusName;
    @ApiModelProperty(value = "图片设计类型")
    private Integer picDesignType;
    @ApiModelProperty(value = "竞价状态")
    private String picDesignTypeName;
    @ApiModelProperty(value = "图片是否设置")
    private Integer picSetType;
    @ApiModelProperty(value = "图片是否设置")
    private String picSetTypeName;
    @ApiModelProperty(value = "图片审核状态")
    private Integer picAuditStatus;
    @ApiModelProperty(value = "图片审核状态")
    private String picAuditStatusName;

}
