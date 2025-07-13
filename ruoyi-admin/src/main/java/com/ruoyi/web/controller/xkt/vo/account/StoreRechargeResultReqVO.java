package com.ruoyi.web.controller.xkt.vo.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author liangyq
 * @date 2025-05-09
 */
@ApiModel
@Data
public class StoreRechargeResultReqVO {

    @NotEmpty(message = "付款单号不能为空")
    @ApiModelProperty(value = "付款单号", required = true)
    private String financeBillNo;
}
