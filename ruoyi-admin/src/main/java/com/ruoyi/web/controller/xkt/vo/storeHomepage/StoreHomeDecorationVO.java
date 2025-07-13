package com.ruoyi.web.controller.xkt.vo.storeHomepage;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@Data
@ApiModel
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreHomeDecorationVO {

    @ApiModelProperty(value = "档口首页装修大轮播图")
    private List<BigBannerVO> bigBannerList;
    @ApiModelProperty(value = "档口首页装修其它图部分")
    private List<DecorationVO> decorationList;

    @Data
    public static class BigBannerVO {

        @ApiModelProperty(value = "业务类型ID，如果选择：不跳转 不传，选择：跳转店铺，传storeId，选择：跳转商品，传storeProdId")
        private Long bizId;
        @ApiModelProperty(value = "业务名称")
        private String bizName;
        @ApiModelProperty(value = "跳转类型 1. 不跳转 2. 跳转店铺 3. 跳转商品")
        private Integer jumpType;
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
    public static class DecorationVO {

        @ApiModelProperty(value = "文件大小")
        private Long bizId;
        @ApiModelProperty(value = "业务名称")
        private String bizName;
        @ApiModelProperty(value = "文件大小")
        private Integer jumpType;
        @ApiModelProperty(value = "文件类型 2轮播小图 3店家推荐 4人气爆款 5当季新品 6销量排行")
        private Integer fileType;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;

    }


}
