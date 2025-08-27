package com.ruoyi.xkt.dto.storeProductDemand;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Builder
public class StoreProdDemandDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "档口工厂ID")
    private Long storeFactoryId;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "需求列表")
    private List<DetailDTO> detailList;

    @Data
    public static class DetailDTO {
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeProdColorId;
        @ApiModelProperty(value = "档口颜色ID")
        private Long storeColorId;
        @ApiModelProperty(value = "货号")
        private String prodArtNum;
        @ApiModelProperty(value = "颜色")
        private String colorName;
        @ApiModelProperty(value = "是否紧急单", notes = "0=正常,1=紧急")
        private Integer emergency;
        @ApiModelProperty(value = "总的数量")
        private Integer quantity;
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
