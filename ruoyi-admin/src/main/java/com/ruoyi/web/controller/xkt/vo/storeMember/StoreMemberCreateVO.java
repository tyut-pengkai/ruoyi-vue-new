package com.ruoyi.web.controller.xkt.vo.storeMember;

import io.swagger.annotations.ApiModel;
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
@ApiModel("档口购买会员")
@Data
@Accessors(chain = true)
public class StoreMemberCreateVO {

    @NotNull(message = "档口ID不能为空!")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;
    @NotNull(message = "会员金额不能为空!")
    @ApiModelProperty(value = "会员金额")
    private BigDecimal amount;

}
