package com.ruoyi.xkt.manager.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.diagnosis.DiagnosisUtils;
import com.alipay.api.domain.*;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.framework.notice.fs.FsNotice;
import com.ruoyi.xkt.domain.StoreOrderDetail;
import com.ruoyi.xkt.dto.finance.AlipayReqDTO;
import com.ruoyi.xkt.dto.order.StoreOrderExt;
import com.ruoyi.xkt.dto.order.StoreOrderRefund;
import com.ruoyi.xkt.enums.ENetResult;
import com.ruoyi.xkt.enums.EPayChannel;
import com.ruoyi.xkt.enums.EPayPage;
import com.ruoyi.xkt.enums.EPayStatus;
import com.ruoyi.xkt.manager.PaymentManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AliPaymentMangerImpl implements PaymentManager, InitializingBean {

    @Value("${alipay.appId:}")
    private String appId;

    @Value("${alipay.appPrivateKey:}")
    private String appPrivateKey;

    @Value("${alipay.appCertPath:}")
    private String appCertPath;

    @Value("${alipay.rootCertPath:}")
    private String rootCertPath;

    @Value("${alipay.alipayPublicCertPath:}")
    private String alipayPublicCertPath;

    @Value("${alipay.notifyUrl:}")
    private String notifyUrl;

    @Value("${alipay.defaultReturnUrl:}")
    private String defaultReturnUrl;

    @Value("${alipay.orderReturnUrl:}")
    private String orderReturnUrl;

    @Value("${alipay.signType:}")
    private String signType;

    @Value("${alipay.charset:}")
    private String charset;

    @Value("${alipay.gatewayUrl:}")
    private String gatewayUrl;

    @Autowired
    private FsNotice fsNotice;

    private AlipayClient alipayClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        AlipayConfig alipayConfig = new AlipayConfig();
        //设置网关地址
        alipayConfig.setServerUrl(gatewayUrl);
        //设置应用APPID
        alipayConfig.setAppId(appId);
        //设置应用私钥
        alipayConfig.setPrivateKey(appPrivateKey);
        //设置应用公钥证书路径
        alipayConfig.setAppCertPath(appCertPath);
        //设置支付宝公钥证书路径
        alipayConfig.setAlipayPublicCertPath(alipayPublicCertPath);
        //设置支付宝根证书路径
        alipayConfig.setRootCertPath(rootCertPath);
        //设置请求格式，固定值json
        alipayConfig.setFormat(Constants.ALIPAY_DEFAULT_FORMAT);
        //设置字符集
        alipayConfig.setCharset(charset);
        //设置签名类型
        alipayConfig.setSignType(signType);
        //构造client
        alipayClient = new DefaultAlipayClient(alipayConfig);
    }

    @Override
    public EPayChannel channel() {
        return EPayChannel.ALI_PAY;
    }

    @Override
    public String pay(String tradeNo, BigDecimal amount, String subject, EPayPage payPage, Date expireTime,
                      String returnUrl) {
        Assert.notEmpty(tradeNo);
        Assert.notNull(amount);
        Assert.notEmpty(subject);
        Assert.notNull(payPage);
        AlipayReqDTO reqDTO = new AlipayReqDTO();
        reqDTO.setOutTradeNo(tradeNo);
        reqDTO.setTotalAmount(amount.toPlainString());
        reqDTO.setSubject(subject);
        reqDTO.setEnablePayChannels("balance,moneyFund,bankPay,debitCardExpress");
        reqDTO.setTimeExpire(DateUtil.formatDateTime(expireTime));
        if (StrUtil.isBlank(returnUrl)) {
            returnUrl = defaultReturnUrl;
        }
        switch (payPage) {
            case WEB:
                reqDTO.setProductCode("FAST_INSTANT_TRADE_PAY");
                AlipayTradePagePayRequest webReq = new AlipayTradePagePayRequest();
                webReq.setReturnUrl(returnUrl);
                webReq.setNotifyUrl(notifyUrl);
                webReq.setBizContent(JSON.toJSONString(reqDTO));
                try {
                    AlipayTradePagePayResponse webResp = alipayClient.pageExecute(webReq);
                    log.error("WEB支付: {}", webResp.getBody());
                    if (webResp.isSuccess()) {
                        return webResp.getBody();
                    } else {
                        String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(webResp);
                        log.error("WEB支付发起失败: {}", diagnosisUrl);
                    }
                } catch (AlipayApiException e) {
                    log.error("WEB支付发起异常", e);
                }
                break;
            case WAP:
                reqDTO.setProductCode("QUICK_WAP_WAY");
                AlipayTradeWapPayRequest wapReq = new AlipayTradeWapPayRequest();
                wapReq.setReturnUrl(returnUrl);
                wapReq.setNotifyUrl(notifyUrl);
                wapReq.setBizContent(JSON.toJSONString(reqDTO));
                try {
                    AlipayTradeWapPayResponse wapResp = alipayClient.pageExecute(wapReq);
                    log.error("WAP支付: {}", wapResp.getBody());
                    if (wapResp.isSuccess()) {
                        return wapResp.getBody();
                    } else {
                        String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(wapResp);
                        log.error("WAP支付发起失败: {}", diagnosisUrl);
                    }
                } catch (AlipayApiException e) {
                    log.error("WAP支付发起异常", e);
                }
                break;
            case APP:
                reqDTO.setProductCode("QUICK_MSECURITY_PAY");
                AlipayTradeAppPayRequest appReq = new AlipayTradeAppPayRequest();
                appReq.setReturnUrl(returnUrl);
                appReq.setNotifyUrl(notifyUrl);
                appReq.setBizContent(JSON.toJSONString(reqDTO));
                try {
                    AlipayTradeAppPayResponse appResp = alipayClient.sdkExecute(appReq);
                    log.error("APP支付: {}", appResp.getBody());
                    if (appResp.isSuccess()) {
                        return appResp.getBody();
                    } else {
                        String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(appResp);
                        log.error("APP支付发起失败: {}", diagnosisUrl);
                    }
                } catch (AlipayApiException e) {
                    log.error("APP支付发起异常", e);
                }
                break;
            default:
                throw new ServiceException("未知的支付来源");
        }
        //告警
        fsNotice.sendMsg2DefaultChat("支付发起异常", JSON.toJSONString(reqDTO));
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
                "代发订单" + orderExt.getOrder().getOrderNo(), payPage, null, orderReturnUrl);
    }

    @Override
    public boolean refundStoreOrder(StoreOrderRefund orderRefund) {
        Assert.notNull(orderRefund);
        Assert.notNull(orderRefund.getOriginOrder());
        Assert.notNull(orderRefund.getRefundOrder());
        Assert.notEmpty(orderRefund.getRefundOrderDetails());
        Assert.notEmpty(orderRefund.getOriginOrder().getPayTradeNo());
        // 构造请求参数以调用接口
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        // 设置商户订单号
        model.setOutTradeNo(orderRefund.getOriginOrder().getOrderNo());
        // 设置支付宝交易号
//        model.setTradeNo(orderRefund.getOriginOrder().getPayTradeNo());
        // 设置退款金额
        BigDecimal amount = BigDecimal.ZERO;
        for (StoreOrderDetail orderDetail : orderRefund.getRefundOrderDetails()) {
            amount = NumberUtil.add(amount, orderDetail.getTotalAmount());
        }
        model.setRefundAmount(amount.toPlainString());
        // 设置退款原因说明
        model.setRefundReason("正常退款");
        // 设置退款请求号
        model.setOutRequestNo(orderRefund.getRefundOrder().getOrderNo());
        request.setBizModel(model);
        try {
            AlipayTradeRefundResponse response = alipayClient.certificateExecute(request);
            log.info("支付宝退款：{}", response.getBody());
            String fundChange = JSON.parseObject(response.getBody())
                    .getJSONObject("alipay_trade_refund_response")
                    .getString("fund_change");
            if (response.isSuccess()) {
                //退款成功判断说明：接口返回fund_change=Y为退款成功，
                // fund_change=N或无此字段值返回时需通过退款查询接口进一步确认退款状态。
                return "Y".equals(fundChange);
            } else {
                //获取诊断链接
                String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
                log.warn("支付宝退款异常: {}", diagnosisUrl);
                return false;
            }
        } catch (Exception e) {
            log.error("退款异常", e);
        }
        //告警
        fsNotice.sendMsg2DefaultChat("退款发起异常", JSON.toJSONString(orderRefund));
        throw new ServiceException("退款失败");
    }

    @Override
    public ENetResult queryStoreOrderRefundResult(String refundOrderNo, String originOrderNo) {
        Assert.notEmpty(refundOrderNo);
        Assert.notEmpty(originOrderNo);
        // 构造请求参数以调用接口
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        AlipayTradeFastpayRefundQueryModel model = new AlipayTradeFastpayRefundQueryModel();
        model.setOutRequestNo(refundOrderNo);
        model.setOutTradeNo(originOrderNo);
        request.setBizModel(model);
        try {
            AlipayTradeFastpayRefundQueryResponse response = alipayClient.certificateExecute(request);
            log.warn("查询支付宝订单退款结果: {}", response.getBody());
            if (response.isSuccess()) {
                String refundStatus = JSON.parseObject(response.getBody())
                        .getJSONObject("alipay_trade_fastpay_refund_query_response")
                        .getString("refund_status");
                if ("REFUND_SUCCESS".equals(refundStatus)) {
                    return ENetResult.SUCCESS;
                }
                return ENetResult.FAILURE;
            } else {
                //获取诊断链接
                String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
                log.error("查询支付宝订单退款果异常: {}", diagnosisUrl);
                return ENetResult.FAILURE;
            }
        } catch (Exception e) {
            log.error("查询支付宝订单退款结果异常", e);
        }
        //告警
        fsNotice.sendMsg2DefaultChat("查询支付宝订单退款结果异常", JSON.toJSONString(model));
        throw new ServiceException("查询支付宝订单退款结果失败");
    }

    @Override
    public ENetResult queryStoreOrderPayResult(String orderNo) {
        Assert.notEmpty(orderNo);
        // 构造请求参数以调用接口
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(orderNo);
        request.setBizModel(model);
        try {
            AlipayTradeQueryResponse response = alipayClient.certificateExecute(request);
            log.warn("查询订单支付结果: {}", response.getBody());
            if (response.isSuccess()) {
                String tradeStatus = JSON.parseObject(response.getBody())
                        .getJSONObject("alipay_trade_query_response")
                        .getString("trade_status");
                //交易支付成功
                if ("TRADE_SUCCESS".equals(tradeStatus)
                        //交易结束，不可退款
                        || "TRADE_FINISHED".equals(tradeStatus)) {
                    return ENetResult.SUCCESS;
                } else if ("WAIT_BUYER_PAY".equals(tradeStatus)) {
                    return ENetResult.WAIT;
                }
                return ENetResult.FAILURE;
            } else {
                //获取错误码
                String subCode = JSON.parseObject(response.getBody())
                        .getJSONObject("alipay_trade_query_response")
                        .getString("sub_code");
                if ("ACQ.TRADE_NOT_EXIST".equals(subCode)) {
                    //交易不存在
                    return ENetResult.FAILURE;
                } else {
                    //获取诊断链接
                    String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
                    log.error("查询订单支付结果异常: {}", diagnosisUrl);
                }
            }
        } catch (Exception e) {
            log.error("查询订单支付结果异常", e);
        }
        //告警
        fsNotice.sendMsg2DefaultChat("查询订单支付结果异常", JSON.toJSONString(model));
        throw new ServiceException("查询订单支付结果失败");
    }

    @Override
    public boolean transfer(String bizNo, String identity, String realName, BigDecimal amount) {
        Assert.notEmpty(bizNo);
        Assert.notEmpty(identity);
        Assert.notEmpty(realName);
        Assert.notNull(amount);
        Assert.isTrue(NumberUtil.isGreaterOrEqual(amount, Constants.ZERO_POINT_ONE), "提现金额不能低于0.1元");
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
        try {
            AlipayFundTransUniTransferResponse response = alipayClient.certificateExecute(request);
            log.info("支付宝转账: {}", response.getBody());
            if (response.isSuccess()) {
                String status = JSON.parseObject(response.getBody())
                        .getJSONObject("alipay_fund_trans_uni_transfer_response")
                        .getString("status");
                return "SUCCESS".equals(status);
            } else {
                //获取诊断链接
                String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
                log.warn("支付宝转账异常: {}", diagnosisUrl);
                return false;
            }
        } catch (Exception e) {
            log.error("支付宝转账异常", e);
        }
        //告警
        fsNotice.sendMsg2DefaultChat("支付宝转账异常", JSON.toJSONString(model));
        throw new ServiceException("支付宝转账失败");
    }

    @Override
    public ENetResult queryTransferResult(String bizNo) {
        Assert.notEmpty(bizNo);
        // 构造请求参数以调用接口
        AlipayFundTransCommonQueryRequest request = new AlipayFundTransCommonQueryRequest();
        AlipayFundTransCommonQueryModel model = new AlipayFundTransCommonQueryModel();
        // 设置商家侧唯一订单号
        model.setOutBizNo(bizNo);
        // 设置描述特定的业务场景
        model.setBizScene("DIRECT_TRANSFER");
        // 设置业务产品码
        model.setProductCode("TRANS_ACCOUNT_NO_PWD");
        request.setBizModel(model);
        try {
            AlipayFundTransCommonQueryResponse response = alipayClient.certificateExecute(request);
            log.info("查询支付宝转账结果: {}", response.getBody());
            if (response.isSuccess()) {
                String status = JSON.parseObject(response.getBody())
                        .getJSONObject("alipay_fund_trans_common_query_response")
                        .getString("status");
                if ("SUCCESS".equals(status)) {
                    return ENetResult.SUCCESS;
                } else if ("WAIT_PAY".equals(status) || "DEALING".equals(status)) {
                    return ENetResult.WAIT;
                }
                return ENetResult.FAILURE;
            } else {
                //获取错误码
                String subCode = JSON.parseObject(response.getBody())
                        .getJSONObject("alipay_fund_trans_common_query_response")
                        .getString("sub_code");
                if ("ORDER_NOT_EXIST".equals(subCode)) {
                    //交易不存在
                    return ENetResult.FAILURE;
                } else {
                    //获取诊断链接
                    String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
                    log.error("查询支付宝转账结果异常: {}", diagnosisUrl);
                }
            }
        } catch (Exception e) {
            log.error("查询支付宝转账结果异常", e);
        }
        //告警
        fsNotice.sendMsg2DefaultChat("查询支付宝转账结果异常", JSON.toJSONString(model));
        throw new ServiceException("查询支付宝转账结果失败");
    }

}
