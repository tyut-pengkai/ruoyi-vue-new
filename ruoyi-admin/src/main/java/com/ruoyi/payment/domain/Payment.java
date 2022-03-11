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
    private String showType;

    public abstract Object pay(SysSaleOrder sso);

    public Payment() {
        super();
        init();
    }

    public abstract void init();

    public abstract void notify(HttpServletRequest request, HttpServletResponse response);

    public static class ShowType {
        public static String QR = "qr";
        public static String HTML = "html";
        public static String FORWARD = "forward";
    }

    public abstract boolean verify(HttpServletRequest request, HttpServletResponse response) throws Exception;

}
