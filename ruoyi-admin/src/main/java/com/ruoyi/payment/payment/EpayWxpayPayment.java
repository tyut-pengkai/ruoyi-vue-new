package com.ruoyi.payment.payment;

import com.ruoyi.payment.paymentBase.EpayPaymentBase;

public class EpayWxpayPayment extends EpayPaymentBase {

    @Override
    public void init() {
        setCode("epay_wechat");
        setName("易支付-微信支付");
        setIcon("pay-wechat");
        setPayType("wxpay");
        super.init();
    }

}
