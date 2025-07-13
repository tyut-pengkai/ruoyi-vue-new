package com.ruoyi.web.controller.xkt.vo.storeProdStorage;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
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
@ApiModel(value = "新增商品入库")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreProdStorageVO {

    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空!")
    private Long storeId;
    @NotNull(message = "入库类型不能为空!")
    @ApiModelProperty(value = "入库类型", required = true)
    private Integer storageType;
    @Valid
    @NotNull(message = "商品入库明细列表不能为空!")
    @ApiModelProperty(value = "商品入库明细列表", required = true)
    private List<StorageDetailVO> detailList;

    @Data
    @ApiModel
    public static class StorageDetailVO {

        @NotBlank(message = "商品货号不能为空!")
        @ApiModelProperty(value = "商品货号", required = true)
        private String prodArtNum;
        @NotNull(message = "档口商品颜色ID不能为空!")
        @ApiModelProperty(value = "档口商品颜色ID", required = true)
        private Long storeProdColorId;
        @NotNull(message = "档口颜色ID不能为空!")
        @ApiModelProperty(value = "档口颜色ID", required = true)
        private Long storeColorId;
        @NotBlank(message = "颜色名称不能为空!")
        @ApiModelProperty(value = "颜色名称", required = true)
        private String colorName;
        @NotNull(message = "档口商品ID不能为空!")
        @ApiModelProperty(value = "档口商品ID", required = true)
        private Long storeProdId;
        @Min(value = 0, message = "商品数量不能小于0双!")
        @Max(value = 9999, message = "商品数量不能超过9999双!")
        @ApiModelProperty(value = "尺码30")
        private Integer size30;
        @Min(value = 0, message = "商品数量不能小于0双!")
        @Max(value = 9999, message = "商品数量不能超过9999双!")
        @ApiModelProperty(value = "尺码31")
        private Integer size31;
        @Min(value = 0, message = "商品数量不能小于0双!")
        @Max(value = 9999, message = "商品数量不能超过9999双!")
        @ApiModelProperty(value = "尺码32")
        private Integer size32;
        @Min(value = 0, message = "商品数量不能小于0双!")
        @Max(value = 9999, message = "商品数量不能超过9999双!")
        @ApiModelProperty(value = "尺码33")
        private Integer size33;
        @Min(value = 0, message = "商品数量不能小于0双!")
        @Max(value = 9999, message = "商品数量不能超过9999双!")
        @ApiModelProperty(value = "尺码34")
        private Integer size34;
        @Min(value = 0, message = "商品数量不能小于0双!")
        @Max(value = 9999, message = "商品数量不能超过9999双!")
        @ApiModelProperty(value = "尺码35")
        private Integer size35;
        @Min(value = 0, message = "商品数量不能小于0双!")
        @Max(value = 9999, message = "商品数量不能超过9999双!")
        @ApiModelProperty(value = "尺码36")
        private Integer size36;
        @Min(value = 0, message = "商品数量不能小于0双!")
        @Max(value = 9999, message = "商品数量不能超过9999双!")
        @ApiModelProperty(value = "尺码37")
        private Integer size37;
        @Min(value = 0, message = "商品数量不能小于0双!")
        @Max(value = 9999, message = "商品数量不能超过9999双!")
        @ApiModelProperty(value = "尺码38")
        private Integer size38;
        @Min(value = 0, message = "商品数量不能小于0双!")
        @Max(value = 9999, message = "商品数量不能超过9999双!")
        @ApiModelProperty(value = "尺码39")
        private Integer size39;
        @Min(value = 0, message = "商品数量不能小于0双!")
        @Max(value = 9999, message = "商品数量不能超过9999双!")
        @ApiModelProperty(value = "尺码40")
        private Integer size40;
        @Min(value = 0, message = "商品数量不能小于0双!")
        @Max(value = 9999, message = "商品数量不能超过9999双!")
        @ApiModelProperty(value = "尺码41")
        private Integer size41;
        @Min(value = 0, message = "商品数量不能小于0双!")
        @Max(value = 9999, message = "商品数量不能超过9999双!")
        @ApiModelProperty(value = "尺码42")
        private Integer size42;
        @Min(value = 0, message = "商品数量不能小于0双!")
        @Max(value = 9999, message = "商品数量不能超过9999双!")
        @ApiModelProperty(value = "尺码43")
        private Integer size43;
        @ApiModelProperty(value = "总数量")
        private Integer quantity;
        @ApiModelProperty(value = "生产价格")
        private BigDecimal producePrice;
        @ApiModelProperty(value = "总的生产价格")
        private BigDecimal produceAmount;

    }

}
