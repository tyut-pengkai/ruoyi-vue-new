package com.ruoyi.xkt.dto.storeProductStock;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口商品盘点提交")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoreProdCheckStockDTO {

    @ApiModelProperty(value = "档口商品颜色的库存")
    List<SPCSStockDTO> checkStockList;

    @Data
    @ApiModel(value = "盘点数量")
    public static class SPCSStockDTO {
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeProdColorId;
        @ApiModelProperty(value = "尺码尺码")
        private Integer size;
        @ApiModelProperty(value = "盘点库存")
        private Integer stock;
    }


}
