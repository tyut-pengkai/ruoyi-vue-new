package com.ruoyi.payment.paymentBase;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.enums.SaleOrderStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.payment.domain.EasypayConfig;
import com.ruoyi.payment.domain.Payment;
import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.domain.SysSaleOrderItem;
import com.ruoyi.system.domain.SysPayment;
import com.ruoyi.system.service.ISysPaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class EpayPaymentBase extends Payment {
    private String serverUrl;
    private String notifyUrl;
    private String returnUrl;
    private String pid;
    private String key;
    private String payType;
    @Resource
    private ISysPaymentService sysPaymentService;
    @Resource
    private RestTemplate restTemplate;

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    public String getSign(Map<String, String> params) {
        //根据key升序排序
        Map<String, String> sign = sortByKey(params);
        StringBuilder signStr = new StringBuilder();
        //遍历map 转成字符串
        for (Map.Entry<String, String> m : sign.entrySet()) {
            if (StringUtils.isNotBlank(m.getKey()) && StringUtils.isNotBlank(m.getValue()) && !Objects.equals(m.getKey(), "sign") && !Objects.equals(m.getKey(), "sign_type")) {
                signStr.append(m.getKey()).append("=").append(m.getValue()).append("&");
            }
        }
        //去掉最后一个 &
        signStr = new StringBuilder(signStr.substring(0, signStr.length() - 1));
        //最后拼接上KEY
        signStr.append(key);
        System.out.println(signStr);
        //转为MD5
        signStr = new StringBuilder(DigestUtils.md5DigestAsHex(signStr.toString().getBytes()));
        return signStr.toString();
    }

    @Override
    public void init() {
        this.setShowType(ShowType.HTML);
//        if (StringUtils.isBlank(notifyUrl)) {
        SysPayment payment = sysPaymentService.selectSysPaymentByPayCode(this.getCode());
        if (payment == null) {
            throw new ServiceException("支付方式【" + this.getName() + "】未配置参数");
        }
        EasypayConfig config = JSON.parseObject(payment.getConfig(), EasypayConfig.class);
        // 配置参数
        this.serverUrl = config.getServerUrl();
        this.returnUrl = config.getReturnUrl();
        this.pid = config.getPid();
        this.key = config.getKey();
        if (StringUtils.isNotBlank(payment.getIcon())) {
            this.setIcon(payment.getIcon());
        }
        // 回调地址
        notifyUrl = getNotifyUrl(config.getNotifyUrl());
//        returnUrl = getReturnUrl(notifyUrl);
        returnUrl = getReturnUrl(config.getReturnUrl());
    }

    @Override
    public Object pay(SysSaleOrder sso) {

        //参数存入 map
        Map<String, String> sign = new HashMap<>();
        sign.put("pid", this.pid);
        sign.put("type", this.payType);
        sign.put("out_trade_no", sso.getOrderNo());
        sign.put("notify_url", this.notifyUrl);
        sign.put("return_url", this.returnUrl);
        String subject = sso.getOrderNo();
        List<SysSaleOrderItem> itemList = sso.getSysSaleOrderItemList();
        if (itemList.size() > 0) {
            subject = itemList.get(0).getTitle();
        }
        sign.put("name", subject);
        sign.put("money", String.valueOf(sso.getActualFee()));
        String signStr = getSign(sign);
        sign.put("sign_type", "MD5");
        sign.put("sign", signStr);
        StringBuilder s = new StringBuilder();
//        s.append("<form id='paying' action='" + serverUrl + "/submit.php' method='post' target=\"_blank\">"); // 跳转新页面
        s.append("<form id='paying' action='" + this.serverUrl + "/submit.php' method='post'>");
        for (Map.Entry<String, String> m : sign.entrySet()) {
            s.append("<input type='hidden' name='" + m.getKey() + "' value='" + m.getValue() + "'/>");
        }
        s.append("<input type='submit' value='请点击此按钮跳转'>");
        s.append("</form>");
        s.append("<script defer=\"defer\">document.forms['paying'].submit();</script>");
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        map.put("body", s.toString());
        return map;
    }

    private boolean verify(HttpServletRequest request) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        System.out.println(JSON.toJSONString(requestParams));
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
//            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }
        //验证签名
        boolean signVerified = false;
        String sign = getSign(params);
        signVerified = sign.equals(params.get("sign"));
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
//                String total_amount = new String(request.getParameter("money").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                if (trade_status.equals("TRADE_SUCCESS")) {
                    doDeliveryGoods(out_trade_no, trade_no, true);
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
            String request = this.serverUrl + "/api.php?act=order&pid=" + this.pid + "&key=" + this.key + "&out_trade_no=" + sso.getOrderNo();
            log.debug(JSON.toJSONString(request));
            try {
                String responseBody = restTemplate.getForObject(request, String.class);
                log.debug(responseBody);
                if (StringUtils.isNotBlank(responseBody)) {
                    Map response = JSON.parseObject(responseBody, Map.class);
                    Object code = response.get("code");
                    if (code != null && Integer.parseInt(code.toString()) == 1) {
                        String tradeNo = response.get("trade_no").toString();
                        int status = Integer.parseInt(response.get("status").toString());
                        if (status == 1) {
                            doDeliveryGoods(sso.getOrderNo(), tradeNo, false);
                        } else {
                            closeSaleOrder(sso.getOrderNo(), tradeNo);
                        }
                    }
                }
            } catch (Exception e) {
//                throw new ServiceException(e.getMessage(), 400);
                log.error("error:" + e.getMessage());
                closeSaleOrder(sso.getOrderNo(), null);
            }
        }
    }
}
