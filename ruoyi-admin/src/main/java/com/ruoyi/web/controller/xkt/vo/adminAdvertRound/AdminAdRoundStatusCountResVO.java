package com.ruoyi.web.controller.xkt.vo.adminAdvertRound;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author liujiang
 * @version v1.0
 * @date 2025/3/27 15:12
 */
@Data
@ApiModel
@Accessors(chain = true)
public class AdminAdRoundStatusCountResVO {

    @ApiModelProperty(value = "待投放数量")
    private Integer unLaunchCount;
    @ApiModelProperty(value = "已投放数量")
    private Integer launchCount;

}
