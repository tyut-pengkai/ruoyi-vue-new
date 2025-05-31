package com.ruoyi.web.controller.xkt.vo.advertRound.app.index;

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
@ApiModel("APP首页热卖精选右侧固定广告位")
@Data
@Accessors(chain = true)
public class APPIndexHotSaleRightFixVO {

    @ApiModelProperty(value = "2商品")
    private Integer displayType;
    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    @ApiModelProperty(value = "售价")
    private BigDecimal price;
    @ApiModelProperty(value = "商品第一张主图路径")
    private String mainPicUrl;

}
