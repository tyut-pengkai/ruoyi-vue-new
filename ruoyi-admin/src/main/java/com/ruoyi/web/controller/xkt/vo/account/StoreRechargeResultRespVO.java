package com.ruoyi.web.controller.xkt.vo.account;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-05-09
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreRechargeResultRespVO {

    @ApiModelProperty(value = "付款单号")
    private String financeBillNo;

    @ApiModelProperty(value = "是否成功")
    private Boolean success;
}
