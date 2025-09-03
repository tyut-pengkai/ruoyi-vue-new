package com.ruoyi.web.controller.xkt.vo.adminAdvertRound;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
@Accessors(chain = true)

public class AdminAdRoundCancelInterceptVO {

    @NotNull(message = "推广轮次ID不能为空")
    @ApiModelProperty(value = "推广轮次ID", required = true)
    private Long advertRoundId;
    @NotNull(message = "档口ID不能为空")
    @ApiModelProperty(value = "档口ID", required = true)
    private Long storeId;

}
