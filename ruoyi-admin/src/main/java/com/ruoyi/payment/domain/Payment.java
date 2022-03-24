package com.ruoyi.payment.domain;

import com.ruoyi.api.v1.support.BaseAutoAware;
import com.ruoyi.sale.domain.SysSaleOrder;
import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Data
public abstract class Payment extends BaseAutoAware {
    /**
     * 支付方式代码
     */
    private String code;
    /**
     * 支付方式名称
     */
    private String name;
    /**
     * 支付方式ICON
     */
    private String icon;
    /**
     * {@link ShowType}
     */
    private String showType;

    public Payment() {
        super();
        init();
    }

    /**
     * 初始化
     */
    public abstract void init();

    /**
     * 触发支付流程
     *
     * @param sso
     * @return
     */
    public abstract Object pay(SysSaleOrder sso);

    /**
     * 回调通知会触发此函数，在此函数中判断回调订单状态并发货
     *
     * @param request
     * @param response
     */
    public abstract void notify(HttpServletRequest request, HttpServletResponse response);

    /**
     * 未支付订单即将过期时触发，可以在此函数中主动向支付平台查询一次订单状态并发货，防止因未收到交易平台回调通知而漏发订单
     *
     * @param sso
     */
    public abstract void beforeExpire(SysSaleOrder sso);

    /**
     * 付款时的展示方式
     */
    public static class ShowType {
        /**
         * 扫码
         */
        public static String QR = "qr";
        /**
         * 渲染页面
         */
        public static String HTML = "html";
        /**
         * 跳转页面
         */
        public static String FORWARD = "forward";
    }
}
