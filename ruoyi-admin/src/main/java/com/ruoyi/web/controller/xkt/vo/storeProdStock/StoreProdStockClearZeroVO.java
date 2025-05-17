package com.ruoyi.web.controller.xkt.vo.storeProdStock;

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
@ApiModel("档口库存清零")
@Data
public class StoreProdStockClearZeroVO {

    @ApiModelProperty(value = "档口ID", required = true)
    @NotNull(message = "档口ID不能为空")
    private Long storeId;
    @NotNull(message = "商品库存ID列表不能为空")
    @ApiModelProperty(value = "商品货号")
    private List<Long> storeProdStockIdList;

}
