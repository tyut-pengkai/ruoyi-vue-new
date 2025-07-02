package com.ruoyi.web.controller.xkt.vo.storeMember;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@Accessors(chain = true)
public class StoreMemberCreateVO {

    @NotNull(message = "档口ID不能为空!")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @NotNull(message = "支付金额不能为空!")
    @ApiModelProperty(value = "支付金额", required = true)
    private BigDecimal payPrice;
    @NotNull(message = "交易密码不能为空!")
    @ApiModelProperty(value = "交易密码")
    private String transactionPassword;

}
