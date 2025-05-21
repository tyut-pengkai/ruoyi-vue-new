package com.ruoyi.xkt.dto.advertRound.pc.index;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("PC 首页 人气榜")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
public class PCIndexBottomPopularDTO {

    @ApiModelProperty(value = "左侧列表")
    List<PCIBPPopularLeftDTO> leftList;
    @ApiModelProperty(value = "中间列表")
    List<PCIBPPopularMidDTO> midList;
    @ApiModelProperty(value = "右侧商品")
    List<PCIBPPopularRightDTO> rightList;

    @Data
    @ApiModel(value = "左侧列表")
    @Accessors(chain = true)
    public static class PCIBPPopularLeftDTO {
        @ApiModelProperty(value = "1推广图")
        private Integer displayType;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @ApiModelProperty(value = "推广图路径")
        private String fileUrl;
    }

    @Data
    @ApiModel(value = "中间列表")
    @Accessors(chain = true)
    public static class PCIBPPopularMidDTO {
        @ApiModelProperty(value = "1推广图")
        private Integer displayType;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "档口ID")
        private Long storeId;
        @ApiModelProperty(value = "推广图路径")
        private String fileUrl;
    }

    @Data
    @ApiModel(value = "右侧商品")
    @Accessors(chain = true)
    public static class PCIBPPopularRightDTO {
        @ApiModelProperty(value = "2商品")
        private Integer displayType;
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
        @ApiModelProperty(value = "售价")
        private BigDecimal price;
        @ApiModelProperty(value = "商品第一张主图路径")
        private String mainPicUrl;
    }

}
