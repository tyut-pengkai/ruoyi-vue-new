package com.ruoyi.web.controller.xkt.vo.storeHomepage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel

public class StoreHomeDecorationVO {

    @NotNull(message = "档口ID不能为空!")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @NotNull(message = "模板编号不能为空!")
    @ApiModelProperty(value = "模板编号", required = true)
    private Integer templateNum;
    @ApiModelProperty(value = "档口首页装修大轮播图")
    private List<DecorationVO> bigBannerList;
    @ApiModelProperty(value = "档口首页装修其它图部分")
    private List<DecorationVO> decorationList;

    @Data
    public static class DecorationVO {
        @ApiModelProperty(value = "storeProdId")
        private Long storeProdId;
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

}
