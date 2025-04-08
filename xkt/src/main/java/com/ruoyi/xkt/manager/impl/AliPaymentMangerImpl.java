package com.ruoyi.xkt.manager.impl;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.xkt.domain.AlipayCallback;
import com.ruoyi.xkt.dto.order.StoreOrderInfo;
import com.ruoyi.xkt.dto.payment.AlipayReqDTO;
import com.ruoyi.xkt.enums.EPayChannel;
import com.ruoyi.xkt.enums.EPayFrom;
import com.ruoyi.xkt.manager.PaymentManager;
import com.ruoyi.xkt.service.IAlipayCallbackService;
import com.ruoyi.xkt.service.IStoreOrderService;
import io.jsonwebtoken.lang.Assert;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author liangyq
 * @date 2025-04-06 19:36
 */
@Slf4j
@Getter
@Component
public class AliPaymentMangerImpl implements PaymentManager {
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

    @Autowired
    private IStoreOrderService storeOrderService;
    @Autowired
    private IAlipayCallbackService alipayCallbackService;

    @Override
    public EPayChannel channel() {
        return EPayChannel.ALI_PAY;
    }

    @Override
    public String payForOrder(Long storeOrderId, EPayFrom payFrom) {
        Assert.notNull(storeOrderId);
        Assert.notNull(payFrom);
        //订单状态更新为支付中
        StoreOrderInfo order = storeOrderService.preparePayOrder(storeOrderId);
        AlipayReqDTO reqDTO = new AlipayReqDTO();
        reqDTO.setOutTradeNo(order.getOrder().getOrderNo());
        reqDTO.setTotalAmount(order.getOrder().getTotalAmount().toPlainString());
        reqDTO.setSubject("代发订单" + order.getOrder().getOrderNo());
        reqDTO.setProductCode("FAST_INSTANT_TRADE_PAY"); //这个是固定的
        String reqStr = JSON.toJSONString(reqDTO);
        AlipayClient alipayClient = new DefaultAlipayClient(gatewayUrl, appId, privateKey, "json", charset,
                alipayPublicKey, signType);
        switch (payFrom) {
            case WEB:
                AlipayTradePagePayRequest webReq = new AlipayTradePagePayRequest();
                webReq.setReturnUrl(returnUrl);
                webReq.setNotifyUrl(notifyUrl);
                webReq.setBizContent(reqStr);
                try {
                    return alipayClient.pageExecute(webReq).getBody();
                } catch (AlipayApiException e) {
                    log.error("支付发起异常", e);
                    throw new ServiceException("支付发起失败");
                }
            case WAP:
                AlipayTradeWapPayRequest wapReq = new AlipayTradeWapPayRequest();
                wapReq.setReturnUrl(returnUrl);
                wapReq.setNotifyUrl(notifyUrl);
                wapReq.setBizContent(reqStr);
                try {
                    return alipayClient.pageExecute(wapReq).getBody();
                } catch (AlipayApiException e) {
                    log.error("支付发起异常", e);
                    throw new ServiceException("支付发起失败");
                }
            default:
                throw new ServiceException("未知的支付来源");
        }
    }

    public void processAlipayCallback(AlipayCallback alipayCallback) {
        AlipayCallback info = alipayCallbackService.getByNotifyId(alipayCallback.getNotifyId());
        if (info == null) {
            //保存到数据库
            info = alipayCallback;
            alipayCallbackService.insertAlipayCallback(info);
        }
        if (info.getRefundFee() != null &&
                !NumberUtil.equals(info.getRefundFee(), BigDecimal.ZERO)) {
            //如果有退款金额，可能是部分退款的回调，这里不做处理
            return;
        }
        //TODO
    }

}
