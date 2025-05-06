package com.ruoyi.payment.payment;

import com.alibaba.fastjson.JSON;
import com.ijpay.core.enums.SignType;
import com.ijpay.core.enums.TradeType;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.WxPayApiConfig;
import com.ijpay.wxpay.WxPayApiConfigKit;
import com.ijpay.wxpay.model.OrderQueryModel;
import com.ijpay.wxpay.model.UnifiedOrderModel;
import com.ruoyi.common.enums.SaleOrderStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.payment.domain.Payment;
import com.ruoyi.payment.domain.WechatpayConfig;
import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.domain.SysSaleOrderItem;
import com.ruoyi.system.domain.SysPayment;
import com.ruoyi.system.service.ISysPaymentService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class WechatpayNativePayment extends Payment {
    private String notifyUrl;
    @Resource
    private ISysPaymentService sysPaymentService;

    @Override
    public void init() {
        this.setCode("wechatpay_native");
        this.setName("微信支付");
        this.setIcon("pay-wechat");
        this.setShowType(ShowType.QR);
//        if (StringUtils.isBlank(notifyUrl)) {
        SysPayment payment = sysPaymentService.selectSysPaymentByPayCode(this.getCode());
        if (payment == null) {
            throw new ServiceException("支付方式【" + this.getName() + "】未配置参数");
        }
        WechatpayConfig config = JSON.parseObject(payment.getConfig(), WechatpayConfig.class);
//        log.debug("微信支付配置：" + payment.getConfig());
        // 配置参数
        if (StringUtils.isNotBlank(payment.getIcon())) {
            this.setIcon(payment.getIcon());
        }
        // 回调地址
        notifyUrl = getNotifyUrl(config.getNotifyUrl());
//        }
        WxPayApiConfig apiConfig = WxPayApiConfig.builder()
                .appId(config.getAppId())
                .mchId(config.getMchId())
                .partnerKey(config.getPartnerKey())
                .build();
        WxPayApiConfigKit.putApiConfig(apiConfig);
    }

    @Override
    public Object pay(SysSaleOrder sso) {

        String ip;
        try {
            ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        } catch (Exception e) {
            ip = "0.0.0.0";
        }
        String subject = sso.getOrderNo();
        List<SysSaleOrderItem> itemList = sso.getSysSaleOrderItemList();
        if (itemList.size() > 0) {
            subject = itemList.get(0).getTitle();
        }
        WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
        Map<String, String> params = UnifiedOrderModel
                .builder()
                .appid(wxPayApiConfig.getAppId())
                .mch_id(wxPayApiConfig.getMchId())
                .nonce_str(WxPayKit.generateStr())
                .body(subject)
                .out_trade_no(sso.getOrderNo())
                .total_fee(String.valueOf(sso.getActualFee().multiply(BigDecimal.valueOf(100)).intValue()))
                .spbill_create_ip(ip)
                .notify_url(notifyUrl)
                .trade_type(TradeType.NATIVE.getTradeType())
                .build()
                .createSign(wxPayApiConfig.getPartnerKey(), SignType.HMACSHA256);
        try {
            log.debug(JSON.toJSONString(params));
            String xmlResult = WxPayApi.pushOrder(false, params);
            Map<String, String> result = WxPayKit.xmlToMap(xmlResult);
            log.debug(JSON.toJSONString(result));
            String returnCode = result.get("return_code");
            String returnMsg = result.get("return_msg");
            String codeUrl = result.get("code_url");
            if (!WxPayKit.codeIsOk(returnCode)) {
                throw new ServiceException("error:" + returnMsg, 400);
            }
            String resultCode = result.get("result_code");
            if (!WxPayKit.codeIsOk(resultCode)) {
                throw new ServiceException("error:" + returnMsg, 400);
            }
            Map<String, Object> map = new HashMap<>();
            map.put("success", true);
            map.put("qrCode", codeUrl);
            return map;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), 400);
        }
    }

    @Override
    public void notify(HttpServletRequest request, HttpServletResponse response) {
        log.info("支付成功, 进入异步通知接口...");
        try {
            String xmlMsg = HttpKit.readData(request);
            Map<String, String> params = WxPayKit.xmlToMap(xmlMsg);
            String returnCode = params.get("return_code");
            // 注意重复通知的情况，同一订单号可能收到多次通知，请注意一定先判断订单状态
            // 注意此处签名方式需与统一下单的签名类型一致
            if (WxPayKit.verifyNotify(params, WxPayApiConfigKit.getWxPayApiConfig().getPartnerKey(), SignType.HMACSHA256)) {
                if (WxPayKit.codeIsOk(returnCode)) {
                    // 更新订单信息
                    String tradeNo = params.get("transaction_id");// 微信交易ID
                    String outTradeNo = params.get("out_trade_no");// 订单号
                    String resultCode = params.get("result_code");// 交易结果
                    if (resultCode.equals("SUCCESS")) {
                        //判断该笔订单是否在商户网站中已经做过处理
                        //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                        //如果有做过处理，不执行商户的业务程序
                        //注意：
                        //付款完成后，支付宝系统发送该交易状态通知
                        // 修改订单状态，改为 支付成功，已付款; 同时新增支付流水
                        doDeliveryGoods(outTradeNo, tradeNo, true);
                        // 反馈给第三方支付平台
                        Map<String, String> xml = new HashMap<>(2);
                        xml.put("return_code", "SUCCESS");
                        xml.put("return_msg", "OK");
                        response.getWriter().write(WxPayKit.toXml(xml));
                    }
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
            try {
                WxPayApiConfig wxPayApiConfig = WxPayApiConfigKit.getWxPayApiConfig();
                Map<String, String> params = OrderQueryModel.builder()
                        .appid(wxPayApiConfig.getAppId())
                        .mch_id(wxPayApiConfig.getMchId())
                        .out_trade_no(sso.getOrderNo())
                        .nonce_str(WxPayKit.generateStr())
                        .build()
                        .createSign(wxPayApiConfig.getPartnerKey(), SignType.MD5);
                log.debug(WxPayKit.toXml(params));
                String query = WxPayApi.orderQuery(params);
                log.debug(query);
                Map<String, String> result = WxPayKit.xmlToMap(query);
                String returnCode = result.get("return_code");
                String returnMsg = result.get("return_msg");
                if (!WxPayKit.codeIsOk(returnCode)) {
                    throw new ServiceException("error:" + returnMsg, 400);
                }
                String resultCode = result.get("result_code");
                if (!WxPayKit.codeIsOk(resultCode)) {
                    throw new ServiceException("error:" + returnMsg, 400);
                }
                String tradeNo = result.get("transaction_id");// 微信交易ID
                String tradeState = result.get("trade_state");// 交易结果
                if (tradeState.equals("SUCCESS")) {
                    doDeliveryGoods(sso.getOrderNo(), tradeNo, false);
                } else {
                    closeSaleOrder(sso.getOrderNo(), tradeNo);
                }
            } catch (Exception e) {
//                throw new ServiceException(e.getMessage(), 400);
                e.printStackTrace();
                log.error("error:" + e.getMessage());
                closeSaleOrder(sso.getOrderNo(), null);
            }
        }
    }
}
