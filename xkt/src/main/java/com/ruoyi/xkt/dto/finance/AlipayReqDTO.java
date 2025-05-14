package com.ruoyi.xkt.dto.finance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liangyq
 * @date 2025-04-07 16:15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlipayReqDTO {
    /**
     * 订单名称
     */
    @JsonProperty("subject")
    private String subject;
    /**
     * 商户网站唯一订单号
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 该笔订单允许的最晚付款时间
     */
    @JsonProperty("timeout_express")
    private String timeoutExpress;
    /**
     * 付款金额
     */
    @JsonProperty("total_amount")
    private String totalAmount;
    /**
     * 销售产品码，与支付宝签约的产品码名称
     */
    @JsonProperty("product_code")
    private String productCode;
    /**
     * 绝对超时时间，格式为yyyy-MM-dd HH:mm:ss
     */
    @JsonProperty("time_expire")
    private String timeExpire;
}
