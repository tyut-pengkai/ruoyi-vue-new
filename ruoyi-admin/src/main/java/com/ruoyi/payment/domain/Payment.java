package com.ruoyi.payment.domain;

import com.ruoyi.api.v1.support.BaseAutoAware;
import com.ruoyi.common.enums.SaleOrderStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.service.ISysSaleOrderService;
import com.ruoyi.sale.service.ISysSaleShopService;
import com.ruoyi.system.domain.SysConfigWebsite;
import com.ruoyi.system.service.ISysConfigWebsiteService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Data
@Slf4j
public abstract class Payment extends BaseAutoAware {

    @Value("${swagger.pathMapping}")
    private String pathMapping;
    private ISysConfigWebsiteService sysConfigWebsiteService;
    private ISysSaleOrderService sysSaleOrderService;
    private ISysSaleShopService sysSaleShopService;

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
         * 渲染div页面
         */
        public static String HTML = "html";
        /**
         * 渲染表单
         */
        public static String FORM = "form";
        /**
         * 跳转页面
         */
        public static String FORWARD = "forward";
    }

    public String getNotifyUrl(String configNotifyUrl) {
        String notifyUrl;
        if (StringUtils.isNotBlank(configNotifyUrl)) {
            notifyUrl = configNotifyUrl;
        } else {
            SysConfigWebsite website = sysConfigWebsiteService.getById(1);
            if (website != null && StringUtils.isNotBlank(website.getDomain())) {
                notifyUrl = website.getDomain();
            } else {
                try {
                    HttpServletRequest request = ServletUtils.getRequest();
                    String port = "80".equals(String.valueOf(request.getServerPort())) ? "" : ":" + request.getServerPort();
                    notifyUrl = request.getScheme() + "://" + request.getServerName() + port;
                } catch (Exception e) {
                    throw new ServiceException("支付方式[" + getName() + "]未配置支付回调接口，加载失败", 400);
                }
            }
        }
        if (notifyUrl.endsWith("/")) {
            notifyUrl = notifyUrl.substring(0, notifyUrl.length() - 1);
        }
        notifyUrl += pathMapping + "/sale/shop/notify/" + getCode();
        return notifyUrl;
    }

    public String getReturnUrl(String configReturnUrl) {
        return getReturnUrl(configReturnUrl, "/sale/shop/return/" + getCode());
    }

    public String getReturnUrl(String configReturnUrl, String defaultApi) {
        String returnUrl = "";
        if (StringUtils.isNotBlank(configReturnUrl)) {
            return configReturnUrl;
        } else {
            SysConfigWebsite website = sysConfigWebsiteService.getById(1);
            if (website != null && StringUtils.isNotBlank(website.getDomain())) {
                returnUrl = website.getDomain();
            } else {
                try {
                    HttpServletRequest request = ServletUtils.getRequest();
                    String port = "80".equals(String.valueOf(request.getServerPort())) ? "" : ":" + request.getServerPort();
                    returnUrl = request.getScheme() + "://" + request.getServerName() + port;
                } catch (Exception ignored) {
                }
            }
        }
        if (returnUrl.endsWith("/")) {
            returnUrl = returnUrl.substring(0, returnUrl.length() - 1);
        }
        returnUrl += defaultApi;
        return returnUrl;
    }

    public void doDeliveryGoods(String outTradeNo, String tradeNo, boolean isCallback) {
        synchronized (tradeNo.intern()) {
            SysSaleOrder sso = sysSaleOrderService.selectSysSaleOrderByOrderNo(outTradeNo);
            if (sso == null) {
                throw new ServiceException("订单不存在", 400);
            }
            if (sso.getStatus() == SaleOrderStatus.WAIT_PAY) {
                sso.setStatus(SaleOrderStatus.PAID);
                sso.setPaymentTime(DateUtils.getNowDate());
                sso.setTradeNo(tradeNo);
                sysSaleOrderService.updateSysSaleOrder(sso);
                // 发货
                sysSaleShopService.deliveryGoods(sso);
                log.info("******************** 支付成功(" + name + (isCallback ? "异步通知" : "主动获取") + ") ********************");
                log.info("* " + name + "交易号: {}", tradeNo);
                log.info("******************** 订单发货(" + name + (isCallback ? "异步通知" : "主动获取") + ") ********************");
            }
        }
    }

    public void closeSaleOrder(String outTradeNo, String tradeNo) {
        SysSaleOrder sso = sysSaleOrderService.selectSysSaleOrderByOrderNo(outTradeNo);
        sso.setStatus(SaleOrderStatus.TRADE_CLOSED);
        sso.setCloseTime(DateUtils.getNowDate());
        sso.setTradeNo(tradeNo);
        sysSaleOrderService.updateSysSaleOrder(sso);
    }
}
