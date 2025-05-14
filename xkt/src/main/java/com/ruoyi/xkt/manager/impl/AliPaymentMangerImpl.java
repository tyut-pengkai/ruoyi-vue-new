package com.ruoyi.xkt.manager.impl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.diagnosis.DiagnosisUtils;
import com.alipay.api.domain.AlipayFundTransUniTransferModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.domain.Participant;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.StoreOrderDetail;
import com.ruoyi.xkt.dto.finance.AlipayReqDTO;
import com.ruoyi.xkt.dto.order.StoreOrderExt;
import com.ruoyi.xkt.dto.order.StoreOrderRefund;
import com.ruoyi.xkt.enums.EPayChannel;
import com.ruoyi.xkt.enums.EPayPage;
import com.ruoyi.xkt.enums.EPayStatus;
import com.ruoyi.xkt.manager.PaymentManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author liangyq
 * @date 2025-04-06 19:36
 */
@Slf4j
@Getter
@Component
public class AliPaymentMangerImpl implements PaymentManager {

    private static final String DEFAULT_FORMAT = "json";
    private static final String PAY_PRODUCT_CODE_WEB = "FAST_INSTANT_TRADE_PAY";
    private static final String PAY_PRODUCT_CODE_WAP = "QUICK_WAP_WAY";
    private static final String PAY_PRODUCT_CODE_APP = "QUICK_MSECURITY_PAY";
    /**
     * 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
     */
    @Value("${alipay.appId:9021000144616672}")
    private String appId;
    /**
     * 商户私钥，您的PKCS8格式RSA2私钥
     */
    @Value("${alipay.privateKey:MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC3x52ARikELwpOPvPcpY9jj7aEWIPivfBwkf3YRyUKnM7L6mOTxD1v3b7QLcL1+l0QmvOPSGgt2O3Qbjtvq+5ih4T32TlAzHGe8VqGVqrEv7ANQicsXz600ze5LdxCDRgBZWhO/GroGJKfenHf7K+arN4KXWWmHyZHCYD5z85TEvZZoA9e9MTMskXeDS8+fwP6McuNf4fR07kVuzblks+wWm1PsWfxFM7qNwSYKXbmEoyDe7+M9vBr7cwuj2OB3PEtj/FZjKMxVnFyVm61KiFAGUf3oUqWmPASTPDkFboRCS/njv980hk3sPba/qMDfMEpjoMZrcLBVzoj747oEi/DAgMBAAECggEAcVoXlRSxG7l/278MXl1nUXtEkeCeh+2rLWN+dDV9bUxGaJOLE4sIccUNeg2foGPpnuJTs15vk0endtVmp3weLntz0gMTQxpWQjiPIyi1b2Djz2msC7w7SwCz7+2PWtYEpmfLrFwX/Eubs+2r6vdrYDWbRj1RAuNXkp0UBgDcO3P9AFeGln5gUWxni4biN6B9sGGdsSwcm1A9biBPRsH2zMSVVWkhLVq+S73smm2Sh7WTIVEyeAuWEDeMs5oI2jQjPHMlUKmWW6JZ3uD5xbRm3Nkvve/nvfJ4ZXx58ABTI9EWzcBlEuLYef4T+P9q4KHZQljpJ4UliIxUVIDSk3GNMQKBgQDevjYnLW2FBNDYQvPf6FkD2iNzKFe80cQYR0+/jiSBgkmtQhXZgyJ2ZqQ5t20MjgLpeY0WJL2twBlFcYGico6Vnv+JMiJDtIjTu1UozAqV2VPeJ34nX34nqJptGwehiPXJupAiulAWtvTqsiYWlFHsr3dMtj6z07M+gDV8PU7P/QKBgQDTOCGgdrS3XYD680eH9vGKbH1DpIOMr3JokLk6kv4yui9Jxk5DMCxcJWkEQRnCrw2UYM8PfX17FqXrwhccmR9hskSmmbLONYzk4SDB73fM2nPqBw+VLO6jbWctylUsGaYfVxDOLDWPQkktKUsHK8pQHLNQ4jwlP5SGQUoIM/OqvwKBgHfRrmPIxh9GBeovqeyKmke+Mk+iJgBGfsvooHeUyQJ5yZRP9lz5c7JpaHI7v4d/ZQWfA0wkG3y5115JvshaA2VtEF0HAPOWy/vJy/eUOyV8sObSK8SWU9CVm+yRG7vDZyRLHXnw62AsrvcJOf/vbVp60RwM9RHbEZLPePYKLLkpAoGADPMaDK56ceuHptsXfZyEPopcO7NwZUW0a/jDgnXUo+OKVqmTzsa7UYLxp1MeczMsT/aHe1mkQdGnpoalyBkTNXgqgVRXBBGAa9/plDpMTADwrl50dB7nGpnwg3wuMJ/58V3zJ9DKD9huiBhKA0yKANNhownbyiTVxE1oboxQ2h0CgYEAkV4hjGe4t6mu5rhqvuIZkDskjuSOwO0dZsGXBZ/sOZusKNmskxeNZht4BajnATN4EaPg265k8ytWnOEQH041J8Wac1M+jAQQp6D1nkQWZZlrclTu7xvM+p6ypVtqNFTseIQtEhtyPw+fcsYrXM4u8LHpGlAzlwQ30lFg+K/Q++Y=}")
    private String privateKey;
    /**
     * 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
     */
    @Value("${alipay.alipayPublicKey:MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkiTTeQLIkyVHnYiNmRKEtsdlwKftUySN1SLkrhn6CgmHl5ovjPeYteweZEZmsf2kt6wRnaLkODVP7xUQiRVC2cu6StdJyvDzyiYI00u72PvSOvaWHcpzgKqTFpGiQseJQlHnI8U3ob4PxfJylBy8RDQHG9fZwNY1WOCsnSb3m2ufV1EQIjndzTq13yQE6jCz639rO8atlAG3PtJW/QRiGUzyGaOuKsS4HRzPbbpmVtsXoN76+x+WLWkeqlTBEu35X4Hdbkf1C36wp3b68sI5fVyLksF6elRv/It4aUzjXSXbO/Dx+zvMIN01FgwaFV6nLh++k3qlmo87p4I+hvsGiQIDAQAB}")
    private String alipayPublicKey;
    /**
     * 服务器异步通知页面路径
     */
    @Value("${alipay.notifyUrl:}")
    private String notifyUrl;
    /**
     * 页面跳转同步通知页面路径
     */
    @Value("${alipay.returnUrl:}")
    private String returnUrl;
    /**
     * 签名方式
     */
    @Value("${alipay.signType:RSA2}")
    private String signType;
    /**
     * 字符编码格式
     */
    @Value("${alipay.charset:UTF-8}")
    private String charset;
    /**
     * 支付宝网关
     */
    @Value("${alipay.gatewayUrl:https://openapi-sandbox.dl.alipaydev.com/gateway.do}")
    private String gatewayUrl;

    @Override
    public EPayChannel channel() {
        return EPayChannel.ALI_PAY;
    }

    @Override
    public String pay(String tradeNo, BigDecimal amount, String subject, EPayPage payPage, Date expireTime) {
        Assert.notEmpty(tradeNo);
        Assert.notNull(amount);
        Assert.notEmpty(subject);
        Assert.notNull(payPage);
        AlipayReqDTO reqDTO = new AlipayReqDTO();
        reqDTO.setOutTradeNo(tradeNo);
        reqDTO.setTotalAmount(amount.toPlainString());
        reqDTO.setSubject(subject);
        reqDTO.setTimeExpire(DateUtil.formatDateTime(expireTime));

        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, privateKey, DEFAULT_FORMAT, charset,
                alipayPublicKey, signType);
        switch (payPage) {
            case WEB:
                reqDTO.setProductCode(PAY_PRODUCT_CODE_WEB);
                AlipayTradePagePayRequest webReq = new AlipayTradePagePayRequest();
                webReq.setReturnUrl(returnUrl);
                webReq.setNotifyUrl(notifyUrl);
                webReq.setBizContent(JSON.toJSONString(reqDTO));
                try {
                    AlipayTradePagePayResponse webResp = alipayClient.pageExecute(webReq);
                    if (webResp.isSuccess()) {
                        return webResp.getBody();
                    } else {
                        String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(webResp);
                        log.error("支付发起失败: {}", diagnosisUrl);
                    }
                } catch (AlipayApiException e) {
                    log.error("WEB支付发起异常", e);
                }
                break;
            case WAP:
                reqDTO.setProductCode(PAY_PRODUCT_CODE_WAP);
                AlipayTradeWapPayRequest wapReq = new AlipayTradeWapPayRequest();
                wapReq.setReturnUrl(returnUrl);
                wapReq.setNotifyUrl(notifyUrl);
                wapReq.setBizContent(JSON.toJSONString(reqDTO));
                try {
                    AlipayTradeWapPayResponse wapResp = alipayClient.pageExecute(wapReq);
                    if (wapResp.isSuccess()) {
                        return wapResp.getBody();
                    } else {
                        String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(wapResp);
                        log.error("支付发起失败: {}", diagnosisUrl);
                    }
                } catch (AlipayApiException e) {
                    log.error("WAP支付发起异常", e);
                }
                break;
            case APP:
                reqDTO.setProductCode(PAY_PRODUCT_CODE_APP);
                AlipayTradeAppPayRequest appReq = new AlipayTradeAppPayRequest();
                appReq.setReturnUrl(returnUrl);
                appReq.setNotifyUrl(notifyUrl);
                appReq.setBizContent(JSON.toJSONString(reqDTO));
                try {
                    AlipayTradeAppPayResponse appResp = alipayClient.sdkExecute(appReq);
                    if (appResp.isSuccess()) {
                        return appResp.getBody();
                    } else {
                        String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(appResp);
                        log.error("支付发起失败: {}", diagnosisUrl);
                    }
                } catch (AlipayApiException e) {
                    log.error("APP支付发起异常", e);
                }
                break;
            default:
                throw new ServiceException("未知的支付来源");
        }
        throw new ServiceException("支付发起失败");
    }

    @Override
    public String payStoreOrder(StoreOrderExt orderExt, EPayPage payPage) {
        Assert.notNull(orderExt);
        Assert.notNull(payPage);
        if (!EPayStatus.PAYING.getValue().equals(orderExt.getOrder().getPayStatus())) {
            throw new ServiceException("订单[" + orderExt.getOrder().getOrderNo() + "]支付状态异常");
        }
        return pay(orderExt.getOrder().getOrderNo(), orderExt.getOrder().getTotalAmount(),
                "代发订单" + orderExt.getOrder().getOrderNo(), payPage,
                DateUtil.offset(orderExt.getOrder().getCreateTime(), DateField.HOUR, Constants.PAY_EXPIRE_MAX_HOURS));
    }

    @Override
    public void refundStoreOrder(StoreOrderRefund orderRefund) {
        Assert.notNull(orderRefund);
        Assert.notNull(orderRefund.getOriginOrder());
        Assert.notNull(orderRefund.getRefundOrder());
        Assert.notEmpty(orderRefund.getRefundOrderDetails());
        Assert.notEmpty(orderRefund.getOriginOrder().getPayTradeNo());
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, privateKey, DEFAULT_FORMAT, charset,
                alipayPublicKey, signType);
        // 构造请求参数以调用接口
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        // 设置商户订单号
        model.setOutTradeNo(orderRefund.getOriginOrder().getOrderNo());
        // 设置支付宝交易号
        model.setTradeNo(orderRefund.getOriginOrder().getPayTradeNo());
        // 设置退款金额
        BigDecimal amount = BigDecimal.ZERO;
        for (StoreOrderDetail orderDetail : orderRefund.getRefundOrderDetails()) {
            //TODO 暂时商品金额+快递费一起退，需调整为实际退款金额
            amount = NumberUtil.add(amount, orderDetail.getTotalAmount());
        }
        model.setRefundAmount(amount.toPlainString());
        // 设置退款原因说明
        model.setRefundReason("正常退款");
        // 设置退款请求号
        model.setOutRequestNo(orderRefund.getRefundOrder().getOrderNo());
        try {
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            log.info("支付宝退款：{}", response.getBody());
            if (response.isSuccess()) {
                //TODO 沙箱环境接口不通
                return;
            }
        } catch (Exception e) {
            log.error("退款异常", e);
        }
        throw new ServiceException("退款失败");
    }

    @Override
    public boolean isStoreOrderPaid(String orderNo) {
        Assert.notEmpty(orderNo);
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, privateKey, DEFAULT_FORMAT, charset,
                alipayPublicKey, signType);
        // 构造请求参数以调用接口
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(orderNo);
        request.setBizModel(model);
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
            if (response.isSuccess()) {
                String tradeStatus = JSON.parseObject(response.getBody())
                        .getJSONObject("alipay_trade_query_response")
                        .getString("trade_status");
                //交易支付成功
                if ("TRADE_SUCCESS".equals(tradeStatus)
                        //交易结束，不可退款
                        || "TRADE_FINISHED".equals(tradeStatus)) {
                    return true;
                }
                return false;
            } else {
                //获取诊断链接
                String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
                log.warn("查询订单支付结果异常: {}", diagnosisUrl);
                //获取错误码
                String subCode = JSON.parseObject(response.getBody())
                        .getJSONObject("alipay_trade_query_response")
                        .getString("sub_code");
                if ("ACQ.TRADE_NOT_EXIST".equals(subCode)) {
                    //交易不存在
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("查询订单支付结果异常", e);
        }
        throw new ServiceException("查询订单支付结果失败");
    }

    @Override
    public void transfer(String bizNo, String identity, String realName, BigDecimal amount) {
        Assert.notEmpty(bizNo);
        Assert.notEmpty(identity);
        Assert.notEmpty(realName);
        Assert.notNull(amount);
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, privateKey, DEFAULT_FORMAT, charset,
                alipayPublicKey, signType);
        // 构造请求参数以调用接口
        AlipayFundTransUniTransferRequest request = new AlipayFundTransUniTransferRequest();
        AlipayFundTransUniTransferModel model = new AlipayFundTransUniTransferModel();
        // 设置商家侧唯一订单号
        model.setOutBizNo(bizNo);
        // 设置订单总金额
        model.setTransAmount(amount.toPlainString());
        // 设置描述特定的业务场景
        model.setBizScene("DIRECT_TRANSFER");
        // 设置业务产品码
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        // 设置收款方信息
        Participant payeeInfo = new Participant();
        payeeInfo.setIdentity(identity);
        payeeInfo.setIdentityType("ALIPAY_LOGON_ID");
        payeeInfo.setName(realName);
        model.setPayeeInfo(payeeInfo);
        // 设置业务备注
        model.setRemark("档口提现");
        // 设置转账业务请求的扩展参数
        model.setBusinessParams("{\"payer_show_name_use_alias\":\"true\"}");
        request.setBizModel(model);
        AlipayFundTransUniTransferResponse response;
        try {
            response = alipayClient.certificateExecute(request);
            if (response.isSuccess()) {
                //TODO 接口未调通
                return;
            } else {
                //获取诊断链接
                String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
                log.warn("支付宝转账异常: {}", diagnosisUrl);
            }
        } catch (Exception e) {
            log.error("支付宝转账异常", e);
        }
        throw new ServiceException("支付宝转账失败");
    }

}
