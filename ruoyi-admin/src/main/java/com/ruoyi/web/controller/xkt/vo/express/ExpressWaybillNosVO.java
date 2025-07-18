package com.ruoyi.web.controller.xkt.vo.express;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author liangyq
 * @date 2025-07-18
 */
@ApiModel
@Data
public class ExpressWaybillNosVO {

    @NotEmpty(message = "运单号不能为空")
    @ApiModelProperty(value = "物流运单号（快递单号）")
    private List<String> expressWaybillNos;
}
