package com.ruoyi.xkt.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel
@Data
@Accessors(chain = true)
public class StoreUpdateSpecialDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "年费金额")
    private BigDecimal serviceAmount;
    @ApiModelProperty(value = "会员金额")
    private BigDecimal memberAmount;
    @ApiModelProperty(value = "服务截止时间")
    private Date serviceEndTime;

}
