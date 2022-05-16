package com.ruoyi.payment.payment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.ruoyi.common.enums.SaleOrderStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.payment.domain.AlipayConfig;
import com.ruoyi.payment.domain.Payment;
import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.domain.SysSaleOrderItem;
import com.ruoyi.sale.service.ISysSaleOrderService;
import com.ruoyi.sale.service.ISysSaleShopService;
import com.ruoyi.system.domain.SysPayment;
import com.ruoyi.system.domain.SysWebsite;
import com.ruoyi.system.service.ISysPaymentService;
import com.ruoyi.system.service.ISysWebsiteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class AlipayF2fPaymentSandbox extends Payment {
    private static String notifyUrl = "";
    private static String alipayPublicKey = "";
    private static AlipayClient alipayClient;
    /**
     * 设置请求的统一前缀
     */
    @Value("${swagger.pathMapping}")
    private String pathMapping;
    @Resource
    private ISysSaleOrderService sysSaleOrderService;
    @Resource
    private ISysSaleShopService sysSaleShopService;
    @Resource
    private ISysPaymentService sysPaymentService;
    @Resource
    private ISysWebsiteService sysWebsiteService;

    @Override
    public void init() {
        this.setCode("alipay_f2f_sandbox");
        this.setName("支付宝当面付沙箱");
        this.setIcon("pay-alipay");
        this.setShowType(ShowType.QR);
        if (StringUtils.isBlank(notifyUrl)) {
            SysPayment payment = sysPaymentService.selectSysPaymentByPayCode(this.getCode());
            if (payment == null) {
                throw new ServiceException("支付方式【" + this.getName() + "】未配置参数");
            }
            AlipayConfig config = JSON.parseObject(payment.getConfig(), AlipayConfig.class);
            // 配置参数
            String serverUrl = "https://openapi.alipaydev.com/gateway.do";
            alipayPublicKey = config.getAlipayPublicKey();
            if (StringUtils.isNotBlank(payment.getIcon())) {
                this.setIcon(payment.getIcon());
            }
            // 回调地址
            if (StringUtils.isNotBlank(config.getNotifyUrl())) {
                notifyUrl = config.getNotifyUrl();
            } else {
                SysWebsite website = sysWebsiteService.getById(1);
                if (website != null && StringUtils.isNotBlank(website.getDomain())) {
                    notifyUrl = website.getDomain();
                } else {
                    try {
                        HttpServletRequest request = ServletUtils.getRequest();
                        String port = "80".equals(String.valueOf(request.getServerPort())) ? "" : ":" + request.getServerPort();
                        notifyUrl = request.getScheme() + "://" + request.getServerName() + port;
                    } catch (Exception e) {
                        throw new ServiceException("支付方式[" + this.getName() + "]未配置支付回调接口，加载失败", 400);
                    }
                }
            }
            if (notifyUrl.endsWith("/")) {
                notifyUrl = notifyUrl.substring(0, notifyUrl.length() - 1);
            }
            notifyUrl += pathMapping + "/sale/shop/notify/" + this.getCode();
            alipayClient = new DefaultAlipayClient(serverUrl, config.getAppId(), config.getPrivateKey(), "json", "UTF-8", alipayPublicKey, "RSA2");
        }
    }

    @Override
    public Object pay(SysSaleOrder sso) {
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl("");
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", sso.getOrderNo());
        bizContent.put("total_amount", sso.getActualFee());
        String subject = sso.getOrderNo();
        List<SysSaleOrderItem> itemList = sso.getSysSaleOrderItemList();
        if (itemList.size() > 0) {
            subject = itemList.get(0).getTitle();
        }
        bizContent.put("subject", subject);

        // 商品明细信息，按需传入
        JSONArray goodsDetail = new JSONArray();
        if (itemList.size() > 0) {
            for (SysSaleOrderItem item : itemList) {
                JSONObject goods = new JSONObject();
                goods.put("goods_id", item.getTemplateId());
                goods.put("goods_name", item.getTitle());
                goods.put("goods_category", item.getTemplateType());
                goods.put("quantity", item.getNum());
                goods.put("price", item.getTotalFee());
                goodsDetail.add(goods);
            }
        }
        bizContent.put("goods_detail", goodsDetail);

        try {
            request.setBizContent(bizContent.toString());
            request.setNotifyUrl(notifyUrl);
            log.debug(JSON.toJSONString(request));
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            log.debug(JSON.toJSONString(response));
            return response;
        } catch (AlipayApiException e) {
            throw new ServiceException(e.getMessage(), 400);
        }
    }

    private boolean verify(HttpServletRequest request) throws AlipayApiException {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        System.out.println(JSON.toJSONString(requestParams));
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
//            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }
        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayPublicKey, "UTF-8", "RSA2");
        //——请在这里编写您的程序（以下代码仅作参考）——
           /* 实际验证过程建议商户务必添加以下校验：
           1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
           2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
           3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
           4、验证app_id是否为该商户本身。
           */
        //验证成功
        if (signVerified) {
            return true;
        } else { //验证失败
            throw new ServiceException("支付验签失败，请联系管理员");
        }
    }

    @Override
    public void notify(HttpServletRequest request, HttpServletResponse response) {
        log.info("支付成功, 进入异步通知接口...");
        try {
            if (verify(request)) {
                //商户订单号
                String out_trade_no = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                //支付宝交易号
                String trade_no = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                //交易状态
                String trade_status = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                //付款金额
                String total_amount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                if (trade_status.equals("WAIT_BUYER_PAY")) {
                    //等待支付
                } else if (trade_status.equals("TRADE_FINISHED")) {
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //如果有做过处理，不执行商户的业务程序
                    //注意： 订单没有退款功能, 这个条件判断是进不来的, 所以此处不必写代码
                    //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                } else if (trade_status.equals("TRADE_SUCCESS")) {
                    //判断该笔订单是否在商户网站中已经做过处理
                    //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                    //如果有做过处理，不执行商户的业务程序
                    //注意：
                    //付款完成后，支付宝系统发送该交易状态通知
                    // 修改订单状态，改为 支付成功，已付款; 同时新增支付流水
                    SysSaleOrder sso = sysSaleOrderService.selectSysSaleOrderByOrderNo(out_trade_no);
                    if (sso == null) {
                        throw new ServiceException("订单不存在", 400);
                    }
                    if (sso.getStatus() == SaleOrderStatus.WAIT_PAY) {
                        sso.setStatus(SaleOrderStatus.PAID);
                        sso.setPaymentTime(DateUtils.getNowDate());
                        sso.setTradeNo(trade_no);
                        sysSaleOrderService.updateSysSaleOrder(sso);
                        // 发货
                        sysSaleShopService.deliveryGoods(sso);
                        log.info("******************** 支付成功(支付宝异步通知) ********************");
                        log.info("* 支付宝交易号: {}", trade_no);
                        log.info("******************** 订单发货(支付宝异步通知) ********************");
                    }
                    // 反馈给第三方支付平台
                    response.getWriter().write("success");
                }
            }
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), 400);
        }
    }

    @Override
    public void beforeExpire(SysSaleOrder sso) {
//        System.out.println("主动查询" + orderNo);
        if (sso != null && SaleOrderStatus.WAIT_PAY.equals(sso.getStatus())) {
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            JSONObject bizContent = new JSONObject();
            bizContent.put("out_trade_no", sso.getOrderNo());
            try {
                request.setBizContent(bizContent.toString());
                request.setNotifyUrl(notifyUrl);
                log.debug(JSON.toJSONString(request));
                AlipayTradeQueryResponse response = alipayClient.execute(request);
                log.debug(JSON.toJSONString(response));
                String tradeNo = response.getTradeNo();
                String status = response.getTradeStatus();
                // 交易状态：
                // WAIT_BUYER_PAY（交易创建，等待买家付款）、
                // TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、
                // TRADE_SUCCESS（交易支付成功）、
                // TRADE_FINISHED（交易结束，不可退款）
                if ("TRADE_SUCCESS".equals(status)) {
                    sso.setStatus(SaleOrderStatus.PAID);
                    sso.setPaymentTime(DateUtils.getNowDate());
                    sso.setTradeNo(tradeNo);
                    sysSaleOrderService.updateSysSaleOrder(sso);
                    // 发货
                    sysSaleShopService.deliveryGoods(sso);
                    log.info("******************** 支付成功(主动查询支付宝) ********************");
                    log.info("* 支付宝交易号: {}", tradeNo);
                    log.info("******************** 订单发货(主动查询支付宝) ********************");
                } else {
                    sso.setStatus(SaleOrderStatus.TRADE_CLOSED);
                    sso.setCloseTime(DateUtils.getNowDate());
                    sso.setTradeNo(tradeNo);
                    sysSaleOrderService.updateSysSaleOrder(sso);
                }
            } catch (AlipayApiException e) {
                throw new ServiceException(e.getMessage(), 400);
            }
        }
    }
}
