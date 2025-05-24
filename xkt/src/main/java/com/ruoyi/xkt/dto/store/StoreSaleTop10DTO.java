package com.ruoyi.xkt.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口前10销售额")
@Data
@Accessors(chain = true)
public class StoreSaleTop10DTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "查询开始时间")
    private Date voucherDateStart;
    @ApiModelProperty(value = "查询结束时间")
    private Date voucherDateEnd;

}
