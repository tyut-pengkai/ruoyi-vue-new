package com.ruoyi.web.controller.xkt.vo.advertRound;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("获取该推广位最新的购买价格及设置的商品")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdRoundLatestVO {

    @NotNull(message = "档口ID不能为空!")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @NotNull(message = "广告类型ID不能为空!")
    @ApiModelProperty(value = "广告类型ID", required = true)
    private Long advertId;
    @NotNull(message = "播放轮次ID不能为空!")
    @ApiModelProperty(value = "播放轮次ID", required = true)
    private Integer roundId;
    @NotNull(message = "展示类型不能为空!")
    @ApiModelProperty(value = "展示类型 时间范围 位置枚举", required = true)
    private Integer showType;
    @ApiModelProperty(value = "位置 A B C D E")
    private String position;
    @NotBlank(message = "对象锁符号不能为空!")
    @ApiModelProperty(value = "对象锁符号")
    private String symbol;
    @NotNull(message = "当前播放状态不能为空!")
    @ApiModelProperty(value = "当前播放状态")
    private Integer launchStatus;

}
