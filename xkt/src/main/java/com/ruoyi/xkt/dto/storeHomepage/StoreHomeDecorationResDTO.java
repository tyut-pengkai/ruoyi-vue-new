package com.ruoyi.xkt.dto.storeHomepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口首页装修")
@Data

public class StoreHomeDecorationResDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口首页装修模板Num")
    private Integer templateNum;
    @ApiModelProperty(value = "档口首页装修大轮播图")
    private List<DecorationDTO> bigBannerList;
    @ApiModelProperty(value = "档口首页装修其它图部分")
    private List<DecorationDTO> decorationList;

    @Data
    @ApiModel(value = "档口首页各模块")
    @Accessors(chain = true)
    public static class DecorationDTO {
        @ApiModelProperty(value = "storeProdId")
        private Long storeProdId;
        @ApiModelProperty(value = "业务名称")
        private String bizName;
        @ApiModelProperty(value = "1.档口（推广图） 2.商品  10.不跳转")
        private Integer displayType;
        @ApiModelProperty(value = "文件路径")
        private String fileUrl;
        @ApiModelProperty(value = "文件类型 1轮播大图")
        private Integer fileType;
        @ApiModelProperty(value = "排序")
        private Integer orderNum;
    }


}
