package com.ruoyi.payment.payment;

import com.ruoyi.payment.paymentBase.EpayPaymentBase;

public class EpayWxpayPayment extends EpayPaymentBase {

    @Override
    public void init() {
        super.setCode("epay_wechat");
        super.setName("易支付-微信支付");
        super.setIcon("pay-wechat");
        super.setPayType("wxpay");
        super.init();
    }

}
