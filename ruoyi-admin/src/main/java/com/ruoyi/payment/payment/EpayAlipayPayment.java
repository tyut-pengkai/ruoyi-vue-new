package com.ruoyi.payment.payment;

import com.ruoyi.payment.paymentBase.EpayPaymentBase;

public class EpayAlipayPayment extends EpayPaymentBase {

    @Override
    public void init() {
        setCode("epay_alipay");
        setName("易支付-支付宝");
        setIcon("pay-alipay");
        setPayType("alipay");
        super.init();
    }

}
