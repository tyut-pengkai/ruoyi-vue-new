package com.ruoyi.web.controller.xkt.vo.express;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liangyq
 * @date 2025-07-18
 */
@ApiModel
@Data
public class ExpressWaybillNoInfoVO {

    @ApiModelProperty(value = "物流ID")
    private Long expressId;

    @ApiModelProperty(value = "物流名称")
    private String expressName;

    @ApiModelProperty(value = "物流运单号（快递单号）")
    private String expressWaybillNo;
}
