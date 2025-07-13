package com.ruoyi.web.controller.xkt.vo.storeCustomer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
public class StoreCusAddOverPriceVO {

    @NotNull(message = "档口ID不能为空!")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @NotNull(message = "档口客户ID不可为空!")
    @ApiModelProperty(value = "档口客户ID", required = true)
    private Long storeCusId;
    @NotNull(message = "是否大小码加价不为空!")
    @ApiModelProperty(value = "是否大小码加价", required = true)
    private Integer addOverPrice;
}
