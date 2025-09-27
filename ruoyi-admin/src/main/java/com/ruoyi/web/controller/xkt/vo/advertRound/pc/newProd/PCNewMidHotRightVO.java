package com.ruoyi.web.controller.xkt.vo.advertRound.pc.newProd;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("PC 新品馆 热卖榜右侧商品")
@Data
public class PCNewMidHotRightVO {

    @ApiModelProperty(value = "2商品")
    private Integer displayType;
    @ApiModelProperty(value = "档口商品ID")
    private Long storeProdId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "排序")
    private Integer orderNum;
    @ApiModelProperty(value = "售价")
    private BigDecimal price;
    @ApiModelProperty(value = "商品第一张主图路径")
    private String mainPicUrl;


}
