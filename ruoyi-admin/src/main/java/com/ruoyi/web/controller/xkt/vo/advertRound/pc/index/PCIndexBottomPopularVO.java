package com.ruoyi.web.controller.xkt.vo.advertRound.pc.index;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("PC 首页 人气榜")
@Data

public class PCIndexBottomPopularVO {

    @ApiModelProperty(value = "左侧列表")
    List<PCIBPPopularLeftVO> leftList;
    @ApiModelProperty(value = "中间列表")
    List<PCIBPPopularMidVO> midList;
    @ApiModelProperty(value = "右侧商品")
    List<PCIBPPopularRightVO> rightList;

    @Data
    public static class PCIBPPopularLeftVO {
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
    public static class PCIBPPopularMidVO {
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
    public static class PCIBPPopularRightVO {
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
