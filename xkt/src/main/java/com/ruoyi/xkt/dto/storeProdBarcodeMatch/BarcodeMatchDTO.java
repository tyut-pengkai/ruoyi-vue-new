package com.ruoyi.xkt.dto.storeProdBarcodeMatch;

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
@ApiModel("档口商品条码匹配")
@Data
public class BarcodeMatchDTO {

    @NotNull(message = "档口商品ID不能为空")
    @ApiModelProperty("档口商品ID")
    private Long storeProdId;
    @ApiModelProperty("档口ID")
    @NotNull(message = "档口ID不能为空")
    private Long storeId;
    @ApiModelProperty("与第三方系统匹配规则")
    @NotNull(message = "与第三方系统匹配规则不能为空")
    @Valid
    List<BarcodeMatchColorDTO> colorList;

    @Data
    public static class BarcodeMatchColorDTO {
        @NotNull(message = "颜色ID不能为空")
        @ApiModelProperty("档口颜色ID")
        private Long storeColorId;
        @ApiModelProperty("颜色尺码ID列表")
        @NotNull(message = "颜色尺码ID列表不能为空")
        @Valid
        List<BarcodeMatchSizeDTO> sizeList;
    }

    @Data
    public static class BarcodeMatchSizeDTO {
        @NotNull(message = "尺码不能为空")
        @ApiModelProperty("尺码")
        private Integer size;
        @NotBlank(message = "条码不能为空")
        @ApiModelProperty("条码")
        private String barcode;
    }

}
