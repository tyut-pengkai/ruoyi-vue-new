package com.ruoyi.web.controller.xkt.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-04-02 22:31
 */
@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreOrderPayRespVO {

    @ApiModelProperty(value = "订单ID")
    private Long storeOrderId;

    @ApiModelProperty(value = "支付渠道返回值: 跳转页面数据/签名字符串/支付跳转链接/预支付交易会话标识（根据选择的支付渠道和支付来源确定）")
    private String payRtnStr;

}
