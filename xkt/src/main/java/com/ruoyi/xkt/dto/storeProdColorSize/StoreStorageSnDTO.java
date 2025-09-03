package com.ruoyi.xkt.dto.storeProdColorSize;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)

public class StoreStorageSnDTO {

    @ApiModelProperty(value = "错误列表")
    List<String> failList;
    @ApiModelProperty(value = "成功列表")
    List<SSSDetailDTO> successList;

    @Data
    @Accessors(chain = true)
    public static class SSSDetailDTO {

        @ApiModelProperty(value = "档口商品颜色尺码ID")
        private Long storeProdColorId;
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "档口颜色ID")
        private Long storeColorId;
        @ApiModelProperty(value = "颜色")
        private String colorName;
        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;
        @ApiModelProperty(value = "生产价格")
        private BigDecimal producePrice;
        @ApiModelProperty(value = "尺码")
        private Integer size;
        @ApiModelProperty(value = "条码前缀")
        private String prefixPart;
        @ApiModelProperty(value = "销售条码")
        private String sn;
    }


}
