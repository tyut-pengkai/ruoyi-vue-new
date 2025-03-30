package com.ruoyi.xkt.dto.storeCustomer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口客户分页查询入参")
@Data
public class StoreCusPageDTO {

    private long pageNum;
    private long pageSize;
    @ApiModelProperty(name = "客户名称")
    private String cusName;
    @ApiModelProperty(name = "档口ID")
    private Long storeId;

}
