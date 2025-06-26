package com.ruoyi.xkt.dto.storeMember;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("档口购买会员")
@Data
@Accessors(chain = true)
public class StoreMemberCreateDTO {

    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "支付金额")
    private BigDecimal payPrice;
    @ApiModelProperty(value = "交易密码")
    private String transactionPassword;

}
