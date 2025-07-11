package com.ruoyi.xkt.dto.storeProdColorSize;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "打印条码请求入参")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StorePrintSnDTO {

    @ApiModelProperty(value = "档口ID")
    private String storeId;
    @ApiModelProperty(value = "打印颜色尺码列表")
    private List<SPColorSizeVO> colorSizeList;

    @Data
    @Accessors(chain = true)
    public static class SPColorSizeVO {
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeProdColorId;
        @ApiModelProperty(value = "档口商品ID")
        private Long storeProdId;
        @ApiModelProperty(value = "档口颜色ID")
        private Long storeColorId;
        @ApiModelProperty(value = "打印尺码列表")
        private List<SPSizeVO> sizeQuantityList;
    }

    @Data
    @Accessors(chain = true)
    public static class SPSizeVO {
        @ApiModelProperty(value = "尺码")
        private Integer size;
        @ApiModelProperty(value = "数量")
        private Integer quantity;
    }


}
