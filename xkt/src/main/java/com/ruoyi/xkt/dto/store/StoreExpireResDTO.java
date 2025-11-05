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
@Data
@ApiModel
@Accessors(chain = true)
public class StoreExpireResDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "试用过期时间  正式版过期时间")
    private Date serviceEndTime;
    @ApiModelProperty(value = "试用金额  正式版金额")
    private BigDecimal serviceAmount;
    @ApiModelProperty(value = "会员过期时间")
    private Date memberEndTime;
    @ApiModelProperty(value = "会员金额")
    private BigDecimal memberAmount;

}
