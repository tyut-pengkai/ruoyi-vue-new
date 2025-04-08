package com.ruoyi.xkt.dto.storeHomepage;

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
@ApiModel("档口首页装修")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreHomeDTO {

    @ApiModelProperty(value = "档口首页装修模板Num")
    private Integer templateNum;
    @ApiModelProperty(value = "档口首页装修")
    List<HomePageFileDTO> fileList;

    @Data
    @ApiModel(value = "档口首页文件")
    @Accessors(chain = true)
    public static class HomePageFileDTO {
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
