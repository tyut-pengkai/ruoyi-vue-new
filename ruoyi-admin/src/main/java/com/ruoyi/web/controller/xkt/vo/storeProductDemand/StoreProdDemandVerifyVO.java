package com.ruoyi.web.controller.xkt.vo.storeProductDemand;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
public class StoreProdDemandVerifyVO {

    @NotNull(message = "档口ID不能为空")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @Valid
    @NotNull(message = "需求列表不能为空")
    @ApiModelProperty(value = "需求列表", required = true)
    private List<DetailVO> detailList;

    @Data
    @ApiModel
    public static class DetailVO {
        @NotNull(message = "档口商品ID不能为空")
        @ApiModelProperty(value = "档口商品ID", required = true)
        private Long storeProdId;
        @NotNull(message = "档口商品颜色ID不能为空")
        @ApiModelProperty(value = "档口商品颜色ID", required = true)
        private Long storeProdColorId;
        @NotBlank(message = "货号不能为空")
        @ApiModelProperty(value = "货号", required = true)
        private String prodArtNum;
        @NotBlank(message = "颜色不能为空")
        @ApiModelProperty(value = "颜色", required = true)
        private String colorName;
        @ApiModelProperty(value = "尺码30")
        private Integer size30;
        @ApiModelProperty(value = "尺码31")
        private Integer size31;
        @ApiModelProperty(value = "尺码32")
        private Integer size32;
        @ApiModelProperty(value = "尺码33")
        private Integer size33;
        @ApiModelProperty(value = "尺码34")
        private Integer size34;
        @ApiModelProperty(value = "尺码35")
        private Integer size35;
        @ApiModelProperty(value = "尺码36")
        private Integer size36;
        @ApiModelProperty(value = "尺码37")
        private Integer size37;
        @ApiModelProperty(value = "尺码38")
        private Integer size38;
        @ApiModelProperty(value = "尺码39")
        private Integer size39;
        @ApiModelProperty(value = "尺码40")
        private Integer size40;
        @ApiModelProperty(value = "尺码41")
        private Integer size41;
        @ApiModelProperty(value = "尺码42")
        private Integer size42;
        @ApiModelProperty(value = "尺码43")
        private Integer size43;
    }

}
