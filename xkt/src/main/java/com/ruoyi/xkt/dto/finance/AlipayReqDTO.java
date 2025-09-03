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
    /**
     * https://opendocs.alipay.com/open/common/wifww7
     * <p>
     * 余额	balance
     * 余额宝	moneyFund
     * 网银	bankPay
     * 借记卡快捷	debitCardExpress
     * 信用卡快捷	creditCardExpress
     * 信用卡卡通	creditCardCartoon
     * 信用卡	creditCard
     * 卡通	cartoon
     * 花呗	pcredit
     * 花呗分期	pcreditpayInstallment
     * 信用支付类型（包含 信用卡卡通，信用卡快捷,花呗，花呗分期）	credit_group
     * 红包	coupon
     * 积分	point
     * 优惠（包含实时优惠+商户优惠）	promotion
     * 营销券	voucher
     * 商户优惠	mdiscount
     * 亲密付	honeyPay
     * 商户预存卡	mcard
     * 个人预存卡	pcard
     */
    @JsonProperty("enable_pay_channels")
    private String enablePayChannels;
}
