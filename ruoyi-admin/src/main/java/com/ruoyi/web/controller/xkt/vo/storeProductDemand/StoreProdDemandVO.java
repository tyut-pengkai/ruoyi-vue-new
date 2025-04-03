package com.ruoyi.web.controller.xkt.vo.storeProductDemand;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品生产需求单")
@Data
public class StoreProdDemandVO {

    @NotNull(message = "档口ID不能为空")
    @ApiModelProperty(name = "档口ID")
    private Long storeId;
    @NotNull(message = "档口工厂ID不能为空")
    @ApiModelProperty(name = "档口工厂ID")
    private Long storeFactoryId;
    @NotNull(message = "需求列表不能为空")
    @ApiModelProperty(name = "需求列表")
    private List<DetailVO> detailList;

    @Data
    public static class DetailVO {
        @NotNull(message = "档口商品ID不能为空")
        @ApiModelProperty(name = "档口商品ID")
        private Long storeProdId;
        @NotNull(message = "档口商品颜色ID不能为空")
        @ApiModelProperty(name = "档口商品颜色ID")
        private Long storeProdColorId;
        @NotBlank(message = "货号不能为空")
        @ApiModelProperty(name = "货号")
        private String prodArtNum;
        @NotBlank(message = "颜色不能为空")
        @ApiModelProperty(name = "颜色")
        private String colorName;
        @NotBlank(message = "是否紧急单不能为空")
        @ApiModelProperty(name = "是否紧急单", notes = "0=正常,1=紧急")
        private String emergency;
        @ApiModelProperty(name = "总的数量")
        private Integer quantity;
        @ApiModelProperty(name = "尺码30")
        private Integer size30;
        @ApiModelProperty(name = "尺码31")
        private Integer size31;
        @ApiModelProperty(name = "尺码32")
        private Integer size32;
        @ApiModelProperty(name = "尺码33")
        private Integer size33;
        @ApiModelProperty(name = "尺码34")
        private Integer size34;
        @ApiModelProperty(name = "尺码35")
        private Integer size35;
        @ApiModelProperty(name = "尺码36")
        private Integer size36;
        @ApiModelProperty(name = "尺码37")
        private Integer size37;
        @ApiModelProperty(name = "尺码38")
        private Integer size38;
        @ApiModelProperty(name = "尺码39")
        private Integer size39;
        @ApiModelProperty(name = "尺码40")
        private Integer size40;
        @ApiModelProperty(name = "尺码41")
        private Integer size41;
        @ApiModelProperty(name = "尺码42")
        private Integer size42;
        @ApiModelProperty(name = "尺码43")
        private Integer size43;
    }

}
