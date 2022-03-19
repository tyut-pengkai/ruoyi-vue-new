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
import com.ruoyi.payment.domain.Payment;
import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.domain.SysSaleOrderItem;
import com.ruoyi.sale.service.ISysSaleOrderService;
import com.ruoyi.sale.service.ISysSaleShopService;
import com.ruoyi.system.service.ISysConfigService;
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
public class AlipayPayment extends Payment {

    private static String notifyUrl = "";
    private static AlipayClient alipayClient;
    /**
     * 设置请求的统一前缀
     */
    @Value("${swagger.pathMapping}")
    private String pathMapping;
    private static String serverUrl;
    private static String appId;
    private static String privateKey;
    private static String alipayPublicKey;
    private static String signType;
    @Resource
    private ISysConfigService configService;
    @Resource
    private ISysSaleOrderService sysSaleOrderService;
    @Resource
    private ISysSaleShopService sysSaleShopService;

    @Override
    public void init() {
        if (StringUtils.isBlank(notifyUrl)) {
            if (pathMapping.contains("dev")) {
                // 回调地址
                notifyUrl = "http://1508qs4589.zicp.vip";
                // 配置参数
                serverUrl = "https://openapi.alipaydev.com/gateway.do";
                appId = "2016102000727957";
                privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC0/phi1ABrXfKPbRqkt/w9KBOVh0RO6fCmRxbWxZoy23HsPJB4l9/J55Npaduz9jVdpJWhlIH76TQbL4Aw+vX/bap6mRCS7qy16XovELkqhBEq9kMVz93zuak4lBpJWY5NiMMXGyIdO+zCMv8r+bIS9VlFBPu/dm5YOMQ4FXiNWU/yVlMeit4YMgw6G3IwmiHF/794s3d2m2Kfr2mOPNyx0wSbXMpiCA65Jw5A1iX0LNtCCkkeuvAH99oDyQwirlMhpK/LJr6j8dns7JFGkAtPEyXWuOqdVXikjtPLecV/YOMEym1O0Lfs5aeYVRV2o4ecja/Pdaz/JJBJKhygpOTJAgMBAAECggEBAJbSoYGZUFAoFXzXWiBxAMylnMw5z/5Ci7rD+pA2UeyXWTOWtH0Jcf757qklAWPRg17pS5c9/aNCDZ2p05T9TAjyBeHrsxf9tAZS7PJTaTm4m+XFGNoQQdBbolv3boA5FJAfqxKSFbduvDiH7oNiq7WIpj8RjAdcVU9G3pwtqCuAG0XtWqeQskIr4j7O5e/DeOqsooDIqeVpBNVor47WoJ38fsY1Deixyockag00nSXaT5GBg7pD1ReXBE+l+l/WDah2XDLEr+eW8SyhOJbaP8N46hmc5M6nlk/irQU5Vb1RCVQVOlkNXAWbC7+0xiiSqv3C9c/vYeZIcotT2FsmhJECgYEA9oDafIUTq+eB9FP4sIWPDxripaeclWsbsbYZ0IQMuCwckc767vXVqhQOcfTbmnKPN94oQ3uO7jIBNNGJxFe3nnrJLJO0O2IFcQ7rJWdpaD3sfCCAKvoWJYRk2FE1EXOFimA/CC19R2WWJoAD/f3+t9GUj9ZRi2OZE8Xr3Pr08N8CgYEAu/eouWHHoIEi01Dgc49caMKs/IDNx/jY4GCRUXPpYYiOuzzKw/BZ+qAdDH3vqJ083Eeoi8xGZF9Fxc9Atvl7SHzDOkWjI43LEVeSgbcy+zRvq3szL+YXROpO3DiX3c9WCW+BmI1PqmqDpJhvqjmLbPUKPv3cU8HcHAYcMwOdF1cCgYAvLYQjdtjH+tv9ZiDfsAAsVOnx6H1of4JiZcbVCKDikta49VNDbtuA3KvTFZj+G1TbzXIJUFmPrxRaBoyGfn9PHpLoLDC/eMgv1jodA4jCAbAEJbhCAXFBpvAiEpDEkUaKsFb/+qzSgFfXcILTFsysY7k6OjuLIPnINgYpWgKNIwKBgGcevW/GlvAVKHfp3NlJAxduBd0ZBMv6V3DxSYf4IUci1bse5NaN269Fe+pIhNxqNuNaZLsdPFkAc5TL2OMJB3uDBs/HOHLe7VL8SiHj0ZJC+CiJlFFo18c1DEKAwcAsaTUP+Xcpv1Tszn/UKR6oJzeFTzOzrdY9enXdXEcYamxNAoGAQTS/WT0wzvebALsDcJptn7KeIbzUDEo4mO3nHd5lrMuEYEjK8HwSvqUWip7FgniClXmMiZ76SH8L4KyImwgkFsQrppKCz486vJI7nAtND07fGlOVyugTSxN4AktSU2hFZG5Y1kXMOwSlVL7mojk12jGOZt3cdxVBckcKBtHhHL8=";
                alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgvhIg0spf1cPGjqyNB6Ev2iL3RKG3XPeeH8a6r43I1yLs7OyDoR9eBhS0R2Uwl7yaK6g3mFBPfhLulAfz7Sxymf+H01k+cR2fkjg1oVw7I7dr2xunyZs8oKwKe0WMeEE10T7bJrubp3UAVrV1vKnWyeTMVt/J6+T9+Geo9fDg49VXuwOvTxe4eeZS+QAyhcWYMCk3Amy/ZpRRa4qJ50LFtKeF52CtCWhENW5uQUNadnyv091ssb0nZZAoM02EnfeeC6UxW+x7uPr4QR265+L3TC7c0+21U1OU5iXHI8caX4wprQDxaMnuqJMm0ki2Gri6os+GT051VZ7vJuZW4MJ5QIDAQAB";
                signType = "RSA2";
            } else {
                // 回调地址
                HttpServletRequest request = ServletUtils.getRequest();
                String port = "80".equals(String.valueOf(request.getServerPort())) ? "" : ":" + request.getServerPort();
                notifyUrl = request.getScheme() + "://" + request.getServerName() + port;
                // 配置参数
                serverUrl = configService.selectConfigByKey("payment.alipay.qr.gateway"); // https://openapi.alipay.com/gateway.do
                appId = configService.selectConfigByKey("payment.alipay.qr.appId"); // 2021003122648690
                privateKey = configService.selectConfigByKey("payment.alipay.qr.privateKey");// MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDIXCCEYDTnYTxxxMffAew3RJR+zijQTxRhlj1npasjvVRW7rxQWGx8N5DX2e7do+WrDdEDAlwHazGljXAqfjALkRq/OLcFEqm7x/9ankJhr7xoNhY333pIx+81Zb9q7garEI2BvNjjboWAenP0c+dhoNizwpxg78BqDOSKucPYCVA2RivedGJHWnzgXq+YTRAEVZdkiPXZH+e3VyqOCv07NiT7DzrP3rFQufztdMddxte/efQAm5VIKPsr07xdm15e3JZes8l7XHrNb7gu0xvSDfD9xJWpJR9Sr5rCRmVR6Y5JBZ+QGtOVrN7Bgr9XgwvJio0ZjB4v6/ymdtzdYcERAgMBAAECggEAFqUK68svz4LW4QjbiiHef7SZj+dfB4QYipr/X6qCuCxazuR2liIYSMXC8hJog9ZVS8ro940Zt6Du4IYmyjau2W/R9RDE5qbgVh/ZhXVjjUTeZ2zNgA0a9gTazU8tnjk+ubDKPYKJhNLl9cphNpyu5wLV2yNAp1gRiCri3ab3MoBPnKJa3y5a+nGIabUG/PL8211A23HFHz+kDx3YIfACmZpp/+TzAePKoEBptkUBAa0QweLwuWngUKmLV7m8RMeKToHARKq4LKbUqZcxfmA/IwiWMGXHRwrMHbupm6dN2sVWMGC2Kxe0jyKWBAch5hSnjcRPOPGBl9Gb3q+V/tScEQKBgQD1LIvOFfKOra8ikz/T7CYUrID4bRhQHaoO6Cawv/Cs1CzLEwijTexn9hDOiP+5mS7wCL7w6QudkrJmS1ZEJY3TbKKSXBb0kkVrIZ3KlGi0QZNtLt+XRELHcB3umocgwKLqw0Yix8mgeC6r7rocE4sM+H3yL3bGAEsQRoYYUw52jQKBgQDRNQAp9Neai6dzw9wqBm3FcuxbekULleZV0/8ukkVtwkeKsnTh2xapkq8yPWCnGHj953kmilJaZiYEgKWzKex2y4PqskqLl4YZBdvYMI+/jJ4Ak8KdqVlD7KszsjSE0MJw3VTPfvJW25QSLPw05uiydG2WESXP3NiLUJRKhb4FlQKBgBLSK51Tc/5d+O7XjPPQ0g+OOoxXm6Ey1cY1LhstcOVjmFiyilw29Cn66slgHPl7d+33TekiisC67TULHYE3vM55LXW82gpGXEvgFcPiZrNHwXCFQ6bSF6pFwhZ6CFuMTjVlbjHnUmQeNb7/IYxcN7V0Mf7wg9apWRnTwCGH5rlVAoGAChmg9GWZsyBi6TfffTfqPMoblx8EDlciU6p0e28cYvwqMAwFkJHfOjiWtLo53FdWIAv40V+EMlEULMt5NHklrWaN69rHto2OL88Umg9eIUVMq4J2tt3iLWFTsp874d2iRYip+4qJcKAROf9p/bPYMCVm1QPm624iFjfBsQdb8TECgYEA2bmVpKjkGejydYELVhBXlrddPGteIqwSCycy2JqZ720B7ziiaOYeEwjTc2O3+J3Mt4F6ubLudb/BAfE6nDZatviLFfCU8KTrP7dmjd9Ou0g+M9BOpjIZMhu3LRH1j2mfQ8FxBhnl2jAtADiMw3drzE1KJDxFfd0o9rk7r+58vhw=
                alipayPublicKey = configService.selectConfigByKey("payment.alipay.qr.alipayPublicKey"); // MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAs3TETDWmn6Giiy4tMqGJ+UoQc7Nb9KSavLyeg2RTDOjRhV13lQ9UmcdElh4GQK38ZfwIPVw8plI3DHRuJCkvOvhD+WHbL3rzgYLLgUNDWj6EigJ+l3Tupub+802CGpgh4LbrQldruAFdpk+uR04tl3KjEtJ1Z4XiJZAKdrEVYyCkHxhy7Ji3YNQecrZ+kI5GQ+uhBJzFeo0IKE0Ep4yug4Dk9gGFIc1H+xO2scahOM0yYOju7IBASgr3Y2/g7g8n2BmY+hnudt/BX9ERrcOvN0MDU2T0LOdzHxl1WhgEPK2rJRB72CwlaThZv+OX/m5uLAB23B5c7XKN/p0P7wpm/QIDAQAB
                signType = configService.selectConfigByKey("payment.alipay.qr.signType"); // RSA2
            }
            notifyUrl += pathMapping + "/sale/shop/notify/alipay_qr";
        }
        this.setCode("alipay_qr");
        this.setName("支付宝QR");
        this.setIcon("pay-alipay");
        this.setEncode("UTF-8");
        this.setEnable(true);
        this.setShowType(ShowType.QR);
        alipayClient = new DefaultAlipayClient(serverUrl, appId, privateKey, "json", this.getEncode(), alipayPublicKey, signType);
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
        boolean signVerified = AlipaySignature.rsaCheckV1(params, alipayPublicKey, this.getEncode(), signType);
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
