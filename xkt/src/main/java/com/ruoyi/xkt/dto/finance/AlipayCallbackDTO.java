package com.ruoyi.xkt.dto.finance;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 阿里支付回调信息
 *
 * @author liangyq
 * @date 2025-04-01 11:57:52.300
 **/
@Data
public class AlipayCallbackDTO {
    /**
     * ID
     */
    private Long id;
    /**
     * 通知的类型
     */
    private String notifyType;
    /**
     * 通知校验 ID
     */
    private String notifyId;
    /**
     * 支付宝分配给开发者的应用 ID
     */
    private String appId;
    /**
     * 编码格式，如 utf-8、gbk、gb2312 等
     */
    private String charset;
    /**
     * 调用的接口版本，固定为：1.0
     */
    private String version;
    /**
     * 商家生成签名字符串所使用的签名算法类型，目前支持 RSA2 和 RSA，推荐使用 RSA2
     */
    private String signType;
    /**
     * 详情可查看 异步返回结果的验签
     */
    private String sign;
    /**
     * 支付宝交易凭证号
     */
    private String tradeNo;
    /**
     * 原支付请求的商户订单号
     */
    private String outTradeNo;
    /**
     * 商户业务 ID
     */
    private String outBizNo;
    /**
     * 买家支付宝用户号
     */
    private String buyerId;
    /**
     * 买家支付宝账号
     */
    private String buyerLogonId;
    /**
     * 卖家支付宝用户号
     */
    private String sellerId;
    /**
     * 卖家支付宝账号
     */
    private String sellerEmail;
    /**
     * 交易目前所处的状态[WAIT_BUYER_PAY	交易创建，等待买家付款 TRADE_CLOSED	未付款交易超时关闭，或支付完成后全额退款 TRADE_SUCCESS	交易支付成功 TRADE_FINISHED	交易结束，不可退款]
     */
    private String tradeStatus;
    /**
     * 本次交易支付的订单金额，单位为人民币（元）
     */
    private BigDecimal totalAmount;
    /**
     * 商家在收益中实际收到的款项，单位人民币（元）
     */
    private BigDecimal receiptAmount;
    /**
     * 用户在交易中支付的可开发票的金额
     */
    private BigDecimal invoiceAmount;
    /**
     * 用户在交易中支付的金额
     */
    private BigDecimal buyerPayAmount;
    /**
     * 使用集分宝支付的金额
     */
    private BigDecimal pointAmount;
    /**
     * 退款通知中，返回总退款金额，单位为人民币（元），支持两位小数
     */
    private BigDecimal refundFee;
    /**
     * 商品的标题/交易标题/订单标题/订单关键字等，是请求时对应的参数，原样通知回来
     */
    private String subject;
    /**
     * 订单的备注、描述、明细等。对应请求时的 body 参数，原样通知回来
     */
    private String body;
    /**
     * 该笔交易创建的时间
     */
    private String gmtCreate;
    /**
     * 该笔交易 的买家付款时间
     */
    private String gmtPayment;
    /**
     * 该笔交易的退款时间
     */
    private String gmtRefund;
    /**
     * 该笔交易结束时间
     */
    private String gmtClose;
    /**
     * 支付成功的各个渠道金额信息
     */
    private String fundBillList;
    /**
     * 公共回传参数，如果请求时传递了该参数，则返回给商家时会在异步通知时将该参数原样返回
     */
    private String passbackParams;
    /**
     * 本交易支付时所有优惠券信息
     */
    private String voucherDetailList;
    /**
     * 回调处理状态[1:初始 2:处理中 3:处理成功 4:处理失败]
     */
    private Integer processStatus;
    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private String updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
}
