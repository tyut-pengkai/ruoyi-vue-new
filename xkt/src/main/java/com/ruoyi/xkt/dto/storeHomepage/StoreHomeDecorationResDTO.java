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
public class StoreHomeDecorationResDTO {

    @ApiModelProperty(value = "档口首页装修模板Num")
    private Integer templateNum;
    @ApiModelProperty(value = "档口首页装修大轮播图")
    private List<DecorationDTO> bannerList;
    @ApiModelProperty(value = "档口首页装修其它图部分")
    private List<DecorationDTO> decorationList;

    @Data
    @ApiModel(value = "档口首页各模块")
    @Accessors(chain = true)
    public static class DecorationDTO {

        @ApiModelProperty(value = "业务ID")
        private Long bizId;
        @ApiModelProperty(value = "业务名称")
        private String bizName;
        @ApiModelProperty(value = "文件大小")
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


}
