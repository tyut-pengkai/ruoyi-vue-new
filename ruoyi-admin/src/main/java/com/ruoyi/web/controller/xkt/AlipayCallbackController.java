package com.ruoyi.web.controller.xkt;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.xkt.domain.AlipayCallback;
import com.ruoyi.xkt.domain.FinanceBill;
import com.ruoyi.xkt.domain.StoreOrder;
import com.ruoyi.xkt.enums.EAlipayCallbackBizType;
import com.ruoyi.xkt.enums.EProcessStatus;
import com.ruoyi.xkt.manager.impl.AliPaymentMangerImpl;
import com.ruoyi.xkt.service.IAlipayCallbackService;
import com.ruoyi.xkt.service.IFinanceBillService;
import com.ruoyi.xkt.service.IStoreOrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author liangyq
 * @date 2025-04-07 18:31
 */
@Api(tags = "支付宝回调接口")
@RestController
@RequestMapping("/rest/v1/alipay-callback")
public class AlipayCallbackController extends XktBaseController {

    private static final String SUCCESS = "success";
    private static final String FAILURE = "failure";

    @Autowired
    private IStoreOrderService storeOrderService;
    @Autowired
    private IFinanceBillService financeBillService;
    @Autowired
    private IAlipayCallbackService alipayCallbackService;
    @Autowired
    private AliPaymentMangerImpl paymentManger;

    /**
     * 支付宝服务器异步通知
     * <p>
     * 1. 在进行异步通知交互时，如果支付宝收到的应答不是 success ，支付宝会认为通知失败，会通过一定的策略定期重新发起通知。
     * 通知的间隔频率为：4m、10m、10m、1h、2h、6h、15h。
     * 2. 商家设置的异步地址（notify_url）需保证无任何字符，如空格、HTML 标签，且不能重定向。
     * （如果重定向，支付宝会收不到 success 字符，会被支付宝服务器判定为该页面程序运行出现异常，而重发处理结果通知）
     * 3. 支付宝是用 POST 方式发送通知信息，商户获取参数的方式如下：request.Form("out_trade_no")、$_POST['out_trade_no']。
     * 4. 支付宝针对同一条异步通知重试时，异步通知参数中的 notify_id 是不变的。
     *
     * @param request
     * @return
     */
    @PostMapping("/notify")
    public String notify(HttpServletRequest request) {
        Map<String, String> params = getParamsMap(request);
        boolean signVerified = false;
        try {
            //验证签名
            signVerified = AlipaySignature.rsaCheckV1(params, paymentManger.getAlipayPublicKey(),
                    paymentManger.getCharset(), paymentManger.getSignType());
        } catch (AlipayApiException e) {
            logger.error("支付宝验签异常", e);
        }
        if (signVerified) {
            AlipayCallback alipayCallback = trans2DO(params);
            logger.info("支付宝支付回调数据:{}", JSONUtil.toJsonStr(alipayCallback));
            //需要严格按照如下描述校验通知数据的正确性：
            //3. 校验通知中的 seller_id（或者 seller_email）是否为 out_trade_no 这笔单据的对应的操作方
            // （有的时候，一个商家可能有多个 seller_id/seller_email）。
            //4. 验证 app_id 是否为该商家本身。
            if (!StrUtil.equals(paymentManger.getAppId(), alipayCallback.getAppId())) {
                logger.warn("支付宝回调应用ID异常:{}", params);
                return FAILURE;
            }
            //1. 商家需要验证该通知数据中的 out_trade_no 是否为商家系统中创建的订单号。
            StoreOrder order = storeOrderService.getByOrderNo(alipayCallback.getOutTradeNo());
            if (order != null) {
                //订单支付
                alipayCallback.setBizType(EAlipayCallbackBizType.ORDER_PAY.getValue());
                //2. 判断 total_amount 是否确实为该订单的实际金额（即商家订单创建时的金额）。
                if (!NumberUtil.equals(order.getTotalAmount(), alipayCallback.getTotalAmount())) {
                    logger.warn("支付宝回调订单金额匹配失败:{}", params);
                    return FAILURE;
                }
                //5. 处理支付宝回调信息
                AlipayCallback info = alipayCallbackService.getByNotifyId(alipayCallback.getNotifyId());
                if (info == null) {
                    //保存到数据库
                    info = alipayCallback;
                    alipayCallbackService.insertAlipayCallback(info);
                }
                if (EProcessStatus.isSkip(info.getProcessStatus())){
                    logger.warn("支付回调重复请求处理: {}", info);
                    return SUCCESS;
                }
                if (!"TRADE_SUCCESS".equals(info.getTradeStatus())) {
                    //非交易支付成功的回调不处理
                    alipayCallbackService.noNeedProcess(info);
                    return SUCCESS;
                }
                if (info.getRefundFee() != null && !NumberUtil.equals(info.getRefundFee(), BigDecimal.ZERO)) {
                    //如果有退款金额，可能是部分退款的回调，这里不做处理
                    alipayCallbackService.noNeedProcess(info);
                    return SUCCESS;
                }
                //更新回调状态和订单状态，创建收款单
                alipayCallbackService.processOrderPaid(info);
                return SUCCESS;
            } else {
                //充值业务
                alipayCallback.setBizType(EAlipayCallbackBizType.RECHARGE.getValue());
                //1. 商家需要验证该通知数据中的 out_trade_no 是否为商家系统中创建的订单号。
                FinanceBill bill = financeBillService.getByBillNo(alipayCallback.getOutTradeNo());
                if (bill != null) {
                    //2. 判断 total_amount 是否确实为该订单的实际金额（即商家订单创建时的金额）。
                    if (!NumberUtil.equals(bill.getBusinessAmount(), alipayCallback.getTotalAmount())) {
                        logger.warn("支付宝回调订单金额匹配失败:{}", params);
                        return FAILURE;
                    }
                    //5. 处理支付宝回调信息
                    AlipayCallback info = alipayCallbackService.getByNotifyId(alipayCallback.getNotifyId());
                    if (info == null) {
                        //保存到数据库
                        info = alipayCallback;
                        alipayCallbackService.insertAlipayCallback(info);
                    }
                    if (EProcessStatus.isSkip(info.getProcessStatus())){
                        logger.warn("支付回调重复请求处理: {}", info);
                        return SUCCESS;
                    }
                    if (!"TRADE_SUCCESS".equals(info.getTradeStatus())) {
                        //非交易支付成功的回调不处理
                        alipayCallbackService.noNeedProcess(info);
                        return SUCCESS;
                    }
                    if (info.getRefundFee() != null && !NumberUtil.equals(info.getRefundFee(), BigDecimal.ZERO)) {
                        //如果有退款金额，可能是部分退款的回调，这里不做处理
                        alipayCallbackService.noNeedProcess(info);
                        return SUCCESS;
                    }
                    //更新回调状态，收款单到账
                    alipayCallbackService.processRecharge(info);
                    return SUCCESS;
                }
            }
            logger.warn("支付宝回调订单匹配失败:{}", params);
            return FAILURE;
        }
        logger.warn("支付宝验签未通过:{}", params);
        return FAILURE;
    }


    /**
     * 获取参数
     *
     * @param request
     * @return
     */
    private Map<String, String> getParamsMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }

    /**
     * 转换为do
     *
     * @param params
     * @return
     */
    private AlipayCallback trans2DO(Map<String, String> params) {
        AlipayCallback alipayCallback = new AlipayCallback();
        alipayCallback.setNotifyType(params.get("notify_type"));
        alipayCallback.setNotifyId(params.get("notify_id"));
        alipayCallback.setAppId(params.get("app_id"));
        alipayCallback.setCharset(params.get("charset"));
        alipayCallback.setVersion(params.get("version"));
        alipayCallback.setSignType(params.get("sign_type"));
        alipayCallback.setSign(params.get("sign"));
        alipayCallback.setTradeNo(params.get("trade_no"));
        alipayCallback.setOutTradeNo(params.get("out_trade_no"));
        alipayCallback.setOutBizNo(params.get("out_biz_no"));
        alipayCallback.setBuyerId(params.get("buyer_id"));
        alipayCallback.setBuyerLogonId(params.get("buyer_logon_id"));
        alipayCallback.setSellerId(params.get("seller_id"));
        alipayCallback.setSellerEmail(params.get("seller_email"));
        alipayCallback.setTradeStatus(params.get("trade_status"));
        String totalAmountStr = params.get("total_amount");
        alipayCallback.setTotalAmount(trans2BigDecimal(totalAmountStr));
        String receiptAmountStr = params.get("receipt_amount");
        alipayCallback.setReceiptAmount(trans2BigDecimal(receiptAmountStr));
        String invoiceAmountStr = params.get("invoice_amount");
        alipayCallback.setInvoiceAmount(trans2BigDecimal(invoiceAmountStr));
        String buyerPayAmountStr = params.get("buyer_pay_amount");
        alipayCallback.setBuyerPayAmount(trans2BigDecimal(buyerPayAmountStr));
        String pointAmountStr = params.get("point_amount");
        alipayCallback.setPointAmount(trans2BigDecimal(pointAmountStr));
        String refundFeeStr = params.get("refund_fee");
        alipayCallback.setRefundFee(trans2BigDecimal(refundFeeStr));
        alipayCallback.setSubject(params.get("subject"));
        alipayCallback.setBody(params.get("body"));
        alipayCallback.setGmtCreate(params.get("gmt_create"));
        alipayCallback.setGmtPayment(params.get("gmt_payment"));
        alipayCallback.setGmtRefund(params.get("gmt_refund"));
        alipayCallback.setGmtClose(params.get("gmt_close"));
        alipayCallback.setFundBillList(params.get("fund_bill_list"));
        alipayCallback.setPassbackParams(params.get("passback_params"));
        alipayCallback.setVoucherDetailList(params.get("voucher_detail_list"));
        alipayCallback.setProcessStatus(EProcessStatus.INIT.getValue());
        return alipayCallback;
    }

    /**
     * 转换为BigDecimal
     *
     * @param value
     * @return
     */
    private BigDecimal trans2BigDecimal(String value) {
        if (StrUtil.isBlank(value)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(value);
    }

}
