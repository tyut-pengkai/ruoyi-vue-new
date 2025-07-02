package com.ruoyi.web.controller.xkt.vo.adminAdvertRound;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
public class AdminAdRoundUnsubscribeVO {

    @NotNull(message = "advertRoundId不能为空")
    @ApiModelProperty(value = "推广轮次ID", required = true)
    private Long advertRoundId;
    @NotNull(message = "storeId不能为空")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @ApiModelProperty(value = "扣除金额")
    private BigDecimal deductionFee;

}
