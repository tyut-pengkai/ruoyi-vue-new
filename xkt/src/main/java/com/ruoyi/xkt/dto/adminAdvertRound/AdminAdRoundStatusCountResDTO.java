package com.ruoyi.xkt.dto.adminAdvertRound;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@ApiModel("管理员推广营销列表统计列表")
@Data
@Accessors(chain = true)
public class AdminAdRoundStatusCountResDTO {

    @ApiModelProperty(value = "待投放数量")
    private Integer unLaunchCount;
    @ApiModelProperty(value = "已投放数量")
    private Integer launchCount;

}
