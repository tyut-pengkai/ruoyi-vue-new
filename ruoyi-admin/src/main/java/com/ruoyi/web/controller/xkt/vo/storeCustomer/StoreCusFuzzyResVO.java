package com.ruoyi.web.controller.xkt.vo.storeCustomer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("销售/退货查询客户名称")
@Data
@Builder
@Accessors(chain = true)
public class StoreCusFuzzyResVO {

    @ApiModelProperty("档口客户ID")
    private Long storeCusId;
    @ApiModelProperty("档口ID")
    private Long storeId;
    @ApiModelProperty(name = "客户名称")
    private String cusName;

}
