package com.ruoyi.web.controller.xkt.vo.advertRound;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口购买推广营销位")
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdRoundStoreCreateVO {

    @NotNull(message = "广告ID不能为空!")
    @ApiModelProperty(value = "广告ID")
    private Long advertId;
    @NotNull(message = "广告轮次ID不能为空!")
    @ApiModelProperty(value = "广告轮次ID")
    private Long roundId;
    @NotNull(message = "推广展示类型不能为空!")
    @ApiModelProperty(value = "推广展示类型 时间范围  位置枚举")
    private Integer showType;
    @ApiModelProperty(value = "[不一定传]广告位置 A B C D E")
    private String position;
    @NotNull(message = "推广档口ID不能为空!")
    @ApiModelProperty(value = "推广档口ID")
    private Long storeId;
    @NotNull(message = "推广档口出价不能为空!")
    @ApiModelProperty(value = "推广档口出价")
    private BigDecimal payPrice;
    @ApiModelProperty(value = "图片设计（1 自主设计、2 平台设计）")
    private Integer picDesignType;
    @ApiModelProperty(value = "推广商品ID列表")
    private String prodIdStr;
    @NotBlank(message = "对象锁符号不能为空!")
    @ApiModelProperty(value = "对象锁符号")
    private String symbol;
    @NotBlank(message = "交易密码不能为空!")
    @ApiModelProperty(value = "交易密码")
    private String transactionPassword;

}
