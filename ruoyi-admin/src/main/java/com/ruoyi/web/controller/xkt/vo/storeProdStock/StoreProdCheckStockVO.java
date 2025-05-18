package com.ruoyi.web.controller.xkt.vo.storeProdStock;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
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
public class StoreProdCheckStockVO {

    @NotNull(message = "档口商品颜色的库存不能为空")
    @ApiModelProperty(value = "档口商品颜色的库存")
    List<SPCSStockVO> checkStockList;

    @Data
    @ApiModel(value = "盘点数量")
    @Valid
    public static class SPCSStockVO {
        @NotNull(message = "档口商品颜色ID不能为空")
        @ApiModelProperty(value = "档口商品颜色ID")
        private Long storeProdColorId;
        @NotNull(message = "盘点库存不能为空")
        @ApiModelProperty(value = "尺码尺码")
        private Integer size;
        @NotNull(message = "盘点库存不能为空")
        @ApiModelProperty(value = "盘点库存")
        private Integer stock;
    }


}
