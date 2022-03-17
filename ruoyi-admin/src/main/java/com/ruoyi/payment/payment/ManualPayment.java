package com.ruoyi.payment.payment;

import com.ruoyi.payment.domain.Payment;
import com.ruoyi.sale.domain.SysSaleOrder;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class ManualPayment extends Payment {

    @Override
    public void init() {
        this.setCode("manual");
        this.setName("手动发货");
        this.setIcon(null);
        this.setEncode(null);
        this.setEnable(false);
        this.setShowType(null);
    }

    @Override
    public Object pay(SysSaleOrder sso) {
        return null;
    }

    @Override
    public void notify(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public void beforeExpire(SysSaleOrder sso) {

    }
}
