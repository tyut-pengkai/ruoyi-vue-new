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
@ApiModel("档口首页装修")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreHomeVO {

    @ApiModelProperty(value = "档口首页装修", required = true)
    List<HomePageFileVO> fileList;

    @Data
    @ApiModel(value = "档口首页文件")
    public static class HomePageFileVO {
        @ApiModelProperty(value = "文件名称")
        private String fileName;
        @ApiModelProperty(value = "文件路径")
        private String fileUrl;
        @ApiModelProperty(value = "文件大小")
        private BigDecimal fileSize;
        @ApiModelProperty(value = "文件类型 1轮播大图  2轮播小图 3店家推荐 4人气爆款 5当季新品 6销量排行")
        private Integer fileType;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
    }

}
