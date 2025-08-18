package com.ruoyi.xkt.dto.advertRound;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("获取该推广位最新的购买价格及设置的商品")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdRoundLatestDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "广告类型ID")
    private Long advertId;
    @ApiModelProperty(value = "播放轮次ID")
    private Integer roundId;
    @ApiModelProperty(value = "展示类型 时间范围 位置枚举")
    private Integer showType;
    @ApiModelProperty(value = "位置 A B C D E")
    private String position;
    @ApiModelProperty(value = "对象锁符号")
    private String symbol;
    @ApiModelProperty(value = "当前播放状态")
    private Integer launchStatus;

}
