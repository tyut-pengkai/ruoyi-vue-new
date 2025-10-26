package com.ruoyi.xkt.dto.storeHomepage;

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
@ApiModel("档口首页装修")
@Data

public class StoreHomeDecorationDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "模板编号")
    private Integer templateNum;
    @ApiModelProperty(value = "档口首页装修大轮播图")
    private List<BigBannerDTO> bigBannerList;
    @ApiModelProperty(value = "档口首页装修其它图部分")
    private List<DecorationDTO> decorationList;

    @Data
    @ApiModel(value = "档口首页各模块")
    public static class BigBannerDTO {
        @ApiModelProperty(value = "业务类型ID，如果选择：不跳转 不传，选择：跳转店铺，传storeId，选择：跳转商品，传storeProdId")
        private Long bizId;
        @ApiModelProperty(value = "业务名称")
        private String bizName;
        @ApiModelProperty(value = "1.档口（推广图） 2.商品  10.不跳转")
        private Integer displayType;
        @ApiModelProperty(value = "文件名称")
        private String fileName;
        @ApiModelProperty(value = "文件路径")
        private String fileUrl;
        @ApiModelProperty(value = "文件大小")
        private BigDecimal fileSize;
        @ApiModelProperty(value = "文件类型 1轮播大图")
        private Integer fileType;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
    }

    @Data
    @ApiModel(value = "档口首页各模块")
    public static class DecorationDTO {
        @ApiModelProperty(value = "文件大小")
        private Long bizId;
        @ApiModelProperty(value = "业务名称")
        private String bizName;
        @ApiModelProperty(value = "1.档口（推广图） 2.商品  10.不跳转")
        private Integer displayType;
        @ApiModelProperty(value = "文件类型 2轮播小图 3店家推荐 4人气爆款 5当季新品 6销量排行")
        private Integer fileType;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
    }


}
