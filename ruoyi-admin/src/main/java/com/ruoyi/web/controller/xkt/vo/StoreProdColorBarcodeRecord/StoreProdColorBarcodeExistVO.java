package com.ruoyi.web.controller.xkt.vo.StoreProdColorBarcodeRecord;

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
public class StoreProdColorBarcodeExistVO {

    @NotNull(message = "档口ID不能为空!")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @NotNull(message = "条码列表不能为空!")
    @Valid
    @ApiModelProperty(value = "条码列表", required = true)
    List<BarcodeItemVO> barcodeList;

    @Data
    public static class BarcodeItemVO {
        @NotBlank(message = "商品货号不能为空!")
        @ApiModelProperty(value = "商品货号", required = true)
        private String prodArtNum;
        @NotNull(message = "档口颜色ID不能为空!")
        @ApiModelProperty(value = "档口颜色ID", required = true)
        private Long storeColorId;
        @NotBlank(message = "颜色名称不能为空!")
        @ApiModelProperty(value = "颜色名称", required = true)
        private String colorName;
        @NotNull(message = "档口商品颜色ID不能为空!")
        @ApiModelProperty(value = "档口商品颜色ID", required = true)
        private Long storeProdColorId;
        @NotBlank(message = "条码不能为空!")
        @ApiModelProperty(value = "条码", required = true)
        private String barcode;
    }


}
