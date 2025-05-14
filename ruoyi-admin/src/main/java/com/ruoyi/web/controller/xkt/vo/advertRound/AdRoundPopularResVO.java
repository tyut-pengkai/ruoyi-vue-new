package com.ruoyi.web.controller.xkt.vo.advertRound;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("最受欢迎的8个推广位")
@Data
@Accessors(chain = true)
public class AdRoundPopularResVO {

    @ApiModelProperty(value = "广告ID")
    private Long advertId;
    @ApiModelProperty(value = "广告类型ID")
    private Long typeId;
    @ApiModelProperty(value = "广告类型名称")
    private String typeName;
    @ApiModelProperty(value = "推广展示类型")
    private Integer showType;
    @ApiModelProperty(value = "投放开始时间")
    private String startTime;
    @ApiModelProperty(value = "投放结束时间")
    private String endTime;
    @ApiModelProperty(value = "起拍价格")
    private BigDecimal startPrice;

}
