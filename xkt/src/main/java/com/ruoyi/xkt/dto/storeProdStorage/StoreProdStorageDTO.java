package com.ruoyi.xkt.dto.storeProdStorage;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品入库")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreProdStorageDTO {

    @ApiModelProperty(name = "档口ID")
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @NotBlank(message = "入库类型不能为空!")
    @ApiModelProperty(name = "入库类型")
    private Integer storageType;
    @Valid
    @NotNull(message = "商品入库明细列表不能为空!")
    @ApiModelProperty(name = "商品入库明细列表")
    private List<StorageDetailDTO> detailList;

    @Data
    public static class StorageDetailDTO {

        @NotBlank(message = "商品货号不能为空!")
        @ApiModelProperty(name = "商品货号")
        private String prodArtNum;
        @NotNull(message = "档口商品颜色ID不能为空!")
        @ApiModelProperty(name = "档口商品颜色ID")
        private Long storeProdColorId;
        @NotBlank(message = "颜色名称不能为空!")
        @ApiModelProperty(name = "颜色名称")
        private String colorName;
        @NotNull(message = "档口商品ID不能为空!")
        @ApiModelProperty(name = "档口商品ID")
        private Long storeProdId;
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
        @ApiModelProperty(name = "总数量")
        private Integer quantity;
        @ApiModelProperty(name = "生产价格")
        private BigDecimal producePrice;
        @ApiModelProperty(name = "总的生产价格")
        private BigDecimal produceAmount;

    }

}
