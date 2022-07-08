package com.ruoyi.payment.payment;

import com.ruoyi.payment.paymentBase.EpayPaymentBase;

public class EpayQqpayPayment extends EpayPaymentBase {

    @Override
    public void init() {
        setCode("epay_qq");
        setName("易支付-QQ钱包");
        setIcon("pay-qq");
        setPayType("qqpay");
        super.init();
    }

}
