package com.ruoyi.web.controller.xkt.vo.StoreProdColorBarcodeRecord;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel
@Data
public class StoreProdColorBarcodeExistResVO {

    @ApiModelProperty(value = "商品货号")
    private String prodArtNum;
    @ApiModelProperty(value = "商品颜色名称")
    private String colorName;
    @ApiModelProperty(value = "已有条码")
    private String existBarcode;
    @ApiModelProperty(value = "新条码")
    private String newBarcode;

}
