package com.ruoyi.web.controller.xkt.vo.storeProdBarcodeMatch;

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
@ApiModel
@Data
public class BarcodeMatchVO {

    @NotNull(message = "档口商品ID不能为空")
    @ApiModelProperty(value = "档口商品ID", required = true)
    private Long storeProdId;
    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空")
    private Long storeId;
    @ApiModelProperty(value = "与第三方系统匹配规则", required = true)
    @NotNull(message = "与第三方系统匹配规则不能为空")
    @Valid
    List<BarcodeMatchColorVO> colorList;

    @Data
    @ApiModel
    public static class BarcodeMatchColorVO {
        @NotNull(message = "颜色ID不能为空")
        @ApiModelProperty(value = "档口颜色ID", required = true)
        private Long storeColorId;
        @ApiModelProperty(value = "颜色尺码ID列表", required = true)
        @NotNull(message = "颜色尺码ID列表不能为空")
        @Valid
        List<BarcodeMatchSizeVO> sizeList;
    }

    @Data
    @ApiModel
    public static class BarcodeMatchSizeVO {
        @NotNull(message = "尺码不能为空")
        @ApiModelProperty(value = "尺码", required = true)
        private Integer size;
        @NotBlank(message = "条码不能为空")
        @ApiModelProperty(value = "条码", required = true)
        private String barcode;
    }

}
