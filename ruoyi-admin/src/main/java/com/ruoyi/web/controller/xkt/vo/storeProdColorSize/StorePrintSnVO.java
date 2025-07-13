package com.ruoyi.web.controller.xkt.vo.storeProdColorSize;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel(value = "打印条码请求入参")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StorePrintSnVO {

    @NotNull(message = "档口ID不能为空!")
    @ApiModelProperty(value = "档口ID", required = true)
    private String storeId;
    @Valid
    @NotNull(message = "打印颜色尺码列表不能为空!")
    @ApiModelProperty(value = "打印颜色尺码列表", required = true)
    private List<SPColorSizeVO> colorSizeList;

    @Data
    @ApiModel
    @Accessors(chain = true)
    public static class SPColorSizeVO {
        @NotNull(message = "档口商品颜色ID不能为空!")
        @ApiModelProperty(value = "档口商品颜色ID", required = true)
        private Long storeProdColorId;
        @NotNull(message = "档口商品ID不能为空!")
        @ApiModelProperty(value = "档口商品ID", required = true)
        private Long storeProdId;
        @NotNull(message = "档口商品颜色ID不能为空!")
        @ApiModelProperty(value = "档口颜色ID", required = true)
        private Long storeColorId;
        @NotNull(message = "打印尺码列表不能为空!")
        @Valid
        @ApiModelProperty(value = "打印尺码列表", required = true)
        private List<SPSizeVO> sizeQuantityList;
    }

    @Data
    @ApiModel
    @Accessors(chain = true)
    public static class SPSizeVO {
        @NotNull(message = "尺码不能为空!")
        @ApiModelProperty(value = "尺码", required = true)
        private Integer size;
        @NotNull(message = "数量不能为空!")
        @ApiModelProperty(value = "数量", required = true)
        private Integer quantity;
    }

}
