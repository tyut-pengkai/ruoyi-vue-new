package com.ruoyi.payment.payment;

import com.ruoyi.payment.paymentBase.EpayPaymentBase;

public class EpayAlipayPayment extends EpayPaymentBase {

    @Override
    public void init() {
        super.setCode("epay_alipay");
        super.setName("易支付-支付宝");
        super.setIcon("pay-alipay");
        super.setPayType("alipay");
        super.init();
    }

}
