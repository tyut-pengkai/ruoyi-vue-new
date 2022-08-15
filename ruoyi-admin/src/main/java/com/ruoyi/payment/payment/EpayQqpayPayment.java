package com.ruoyi.payment.payment;

import com.ruoyi.payment.paymentBase.EpayPaymentBase;

public class EpayQqpayPayment extends EpayPaymentBase {

    @Override
    public void init() {
        super.setCode("epay_qq");
        super.setName("易支付-QQ钱包");
        super.setIcon("pay-qq");
        super.setPayType("qqpay");
        super.init();
    }

}
