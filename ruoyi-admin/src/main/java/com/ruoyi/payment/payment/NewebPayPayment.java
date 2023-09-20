package com.ruoyi.payment.payment;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.AES;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.enums.SaleOrderStatus;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.payment.domain.NewebPayConfig;
import com.ruoyi.payment.domain.Payment;
import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.domain.SysSaleOrderItem;
import com.ruoyi.system.domain.SysPayment;
import com.ruoyi.system.service.ISysPaymentService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
public class NewebPayPayment extends Payment {

    private final String Host = "https://core.newebpay.com";
    private NewebPayConfig newebPayConfig;
    @Resource
    private ISysPaymentService sysPaymentService;


    @Override
    public void init() {
        this.setCode("neweb_pay");
        this.setName("藍新金流");
        this.setIcon("pay-newebpay");
        this.setShowType(ShowType.FORM);
        SysPayment payment = sysPaymentService.selectSysPaymentByPayCode(this.getCode());
        if (payment == null) {
            throw new ServiceException("支付方式【" + this.getName() + "】未配置参数");
        }
        newebPayConfig = JSONObject.parseObject(payment.getConfig(), NewebPayConfig.class);

        if (StringUtils.isNotBlank(payment.getIcon())) {
            this.setIcon(payment.getIcon());
        }

    }

    @Override
    public Object pay(SysSaleOrder sso) {
        Map<String, Object> content = new LinkedHashMap();
        content.put("MerchantID", newebPayConfig.getMerchantID());
        content.put("RespondType", "JSON");
        content.put("TimeStamp", System.currentTimeMillis());
        content.put("Version", "2.0");
        content.put("MerchantOrderNo", sso.getOrderNo());
        //不支持小数点,向上取整
        content.put("Amt", sso.getActualFee().setScale(0, BigDecimal.ROUND_UP).intValue());
        content.put("NotifyURL", getNotifyUrl(newebPayConfig.getNotifyUrl()));
        content.put("ReturnURL", getReturnUrl(newebPayConfig.getReturnUrl(), "/queryOrder?orderNo=" + sso.getOrderNo()));
        String subject = sso.getOrderNo();
        List<SysSaleOrderItem> itemList = sso.getSysSaleOrderItemList();
        if (itemList.size() > 0) {
            subject = itemList.get(0).getTitle();
        }
        content.put("ItemDesc", subject);

        try {
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, Object> entry : content.entrySet()) {
                stringBuilder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue().toString(), "UTF-8")).append("&");
            }
            String data = stringBuilder.substring(0, stringBuilder.length() - 1);

            //AES-256-CBC-PKCS7Padding
            AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, newebPayConfig.getHashKey().getBytes(StandardCharsets.UTF_8), newebPayConfig.getHashIV().getBytes(StandardCharsets.UTF_8));
            String tradeInfo = aes.encryptHex(data).toLowerCase(Locale.ROOT);

//            System.out.println("HashKey=" + newebPayConfig.getHashKey() + "&" + tradeInfo + "&HashIV=" + newebPayConfig.getHashIV());

            //SHA256
            String tradeSha = DigestUtil.sha256Hex("HashKey=" + newebPayConfig.getHashKey() + "&" + tradeInfo + "&HashIV=" + newebPayConfig.getHashIV()).toUpperCase(Locale.ROOT);

            Map<String, Object> postData = new LinkedHashMap();
            postData.put("MerchantID", newebPayConfig.getMerchantID());
            postData.put("TradeInfo", tradeInfo);
            postData.put("TradeSha", tradeSha);
            postData.put("Version", "2.0");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("data", postData);
            jsonObject.put("url", Host + "/MPG/mpg_gateway");
            jsonObject.put("success", true);
            return jsonObject;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage(), 400);
        }
    }

    @Override
    public void notify(HttpServletRequest request, HttpServletResponse response) {
        log.info("支付成功, 进入异步通知接口...");
        try {
            if (verify(request)) {
                String tradeInfo = request.getParameter("TradeInfo");
                AES aes = new AES(Mode.CBC, Padding.NoPadding, newebPayConfig.getHashKey().getBytes(StandardCharsets.UTF_8), newebPayConfig.getHashIV().getBytes(StandardCharsets.UTF_8));
                String data = UnPadPKCS7(aes.decrypt(tradeInfo));

                JSONObject jsonObject = JSONObject.parseObject(data);
                String trade_status = jsonObject.getString("Status");
                if (!"SUCCESS".equals(trade_status)) {
                    throw new ServiceException("支付返回失败", 400);
                }

                JSONObject result = jsonObject.getJSONObject("Result");
                String out_trade_no = result.getString("MerchantOrderNo");
                String trade_no = result.getString("TradeNo");
                if (trade_status.equals("SUCCESS")) {
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
        log.info("超时前,检查支付情况...");
        if (sso != null && SaleOrderStatus.WAIT_PAY.equals(sso.getStatus())) {
            Map<String, Object> postData = new LinkedHashMap();
            postData.put("MerchantID", newebPayConfig.getMerchantID());
            postData.put("Version", "1.3");
            postData.put("RespondType", "JSON");

            String CheckValue = "IV=" + newebPayConfig.getHashIV() + "&Amt=" + sso.getActualFee().setScale(0, BigDecimal.ROUND_UP).intValue() +
                    "&MerchantID=" + newebPayConfig.getMerchantID() + "&MerchantOrderNo=" + URLEncoder.encode(sso.getOrderNo()) + "&Key=" + newebPayConfig.getHashKey();
            postData.put("CheckValue", DigestUtil.sha256Hex(CheckValue).toUpperCase(Locale.ROOT));
            postData.put("TimeStamp", System.currentTimeMillis());
            postData.put("MerchantOrderNo", sso.getOrderNo());
            postData.put("Amt", sso.getActualFee().setScale(0, BigDecimal.ROUND_UP).intValue());


            try {
                //订单查询接口
                String result = HttpUtil.post(Host + "/API/QueryTradeInfo", postData);
                JSONObject jsonObject = JSONObject.parseObject(result);
                if (!"SUCCESS".equals(jsonObject.getString("Status"))) {
                    System.out.println("查询失败," + result);
                    closeSaleOrder(sso.getOrderNo(), null);
                    return;
                }
                String tradeNo = jsonObject.getJSONObject("Result").getString("TradeNo");
                Integer status = jsonObject.getJSONObject("Result").getInteger("TradeStatus");
                // 交易状态：
                // 0    未付款
                // 1    付款成功
                // 2    付款失敗
                // 3    取消付款
                // 6    退款
                if (status == 1) {
                    doDeliveryGoods(sso.getOrderNo(), tradeNo, false);
                } else {
                    closeSaleOrder(sso.getOrderNo(), tradeNo);
                }
            } catch (Exception e) {
                log.error("error:" + e.getMessage());
                closeSaleOrder(sso.getOrderNo(), null);
            }
        }
    }

    private boolean verify(HttpServletRequest request) {
//        log.info("验签...");
        String tradeSha = request.getParameter("TradeSha");
        String tradeInfo = request.getParameter("TradeInfo");
        String merchantID = request.getParameter("MerchantID");
        String status = request.getParameter("Status");
        if (!"SUCCESS".equals(status)) {
            throw new ServiceException("回调状态异常");
        }
        if (!newebPayConfig.getMerchantID().equals(merchantID)) {
            throw new ServiceException("商户号异常");
        }

        boolean signVerified = DigestUtil.sha256Hex("HashKey=" + newebPayConfig.getHashKey() + "&" + tradeInfo + "&HashIV=" + newebPayConfig.getHashIV()).toUpperCase(Locale.ROOT).equals(tradeSha);
        if (signVerified) {
            return true;
        } else {
            throw new ServiceException("支付验签失败，请联系管理员");
        }
    }

    private String UnPadPKCS7(byte[] data) {
        int pad = data[data.length - 1];
        byte[] result = new byte[data.length - pad];
        System.arraycopy(data, 0, result, 0, data.length - pad);
        return new String(result, StandardCharsets.UTF_8);
    }
}
