package com.ruoyi.xkt.dto.adminAdvertRound;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("管理员退订")
@Data
public class AdminAdRoundUnsubscribeDTO {

    @ApiModelProperty(value = "推广轮次ID")
    private Long advertRoundId;
    @ApiModelProperty(value = "档口ID")
    private Long storeId;
    @ApiModelProperty(value = "扣除金额")
    private BigDecimal deductionFee;

}
