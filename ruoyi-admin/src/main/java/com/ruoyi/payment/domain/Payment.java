package com.ruoyi.payment.domain;

import com.ruoyi.api.v1.support.BaseAutoAware;
import com.ruoyi.sale.domain.SysSaleOrder;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Data
public abstract class Payment extends BaseAutoAware {

    private String code;
    private String name;
    private String icon;
    private String encode;
    private Boolean enable;

    public Payment() {
        super();
        init();
    }

    public abstract void init();

    public abstract Object payment(SysSaleOrder sso);

    public abstract boolean verify(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
