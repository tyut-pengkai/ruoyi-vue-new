package com.ruoyi.xkt.dto.storeProdColorSize;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreStockTakingSnResDTO {

    @ApiModelProperty(value = "错误列表")
    List<String> failList;
    @ApiModelProperty(value = "成功列表")
    List<SSTSDetailDTO> successList;

    @Data
    @Accessors(chain = true)
    public static class SSTSDetailDTO {
        @ApiModelProperty(value = "档口商品颜色尺码ID")
        private Long storeProdColorId;
        @ApiModelProperty(value = "颜色")
        private String colorName;
        @ApiModelProperty(value = "商品货号")
        private String prodArtNum;
        @ApiModelProperty(value = "尺码")
        private Integer size;
        @ApiModelProperty(value = "盘点库存")
        private Long stock;
    }

}
