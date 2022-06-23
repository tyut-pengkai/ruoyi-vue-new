package com.ruoyi.web.controller.sale.vo;

import java.math.BigDecimal;

public class ChargeOrderVo {

    private BigDecimal amount;
    private String payMode;

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }
}
