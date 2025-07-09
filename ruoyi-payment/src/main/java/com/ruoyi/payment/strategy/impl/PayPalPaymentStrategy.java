package com.ruoyi.payment.strategy.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ijpay.paypal.PayPalApi;
import com.ijpay.paypal.PayPalApiConfig;
import com.ruoyi.payment.config.PayPalConfig;
import com.ruoyi.payment.domain.PaymentOrder;
import com.ruoyi.payment.mapper.PaymentOrderMapper;
import com.ruoyi.payment.model.PaymentResponse;
import com.ruoyi.payment.model.PaymentResult;
import com.ruoyi.payment.service.IPaymentOrderService;
import com.ruoyi.payment.strategy.PaymentStrategy;
import com.ruoyi.payment.util.HttpUtils;
import com.ruoyi.payment.util.PayKit;
import com.ruoyi.payment.util.PayPalApiConfigKit;
import com.ruoyi.payment.util.PayPalApiUrl;
import com.ruoyi.common.utils.DateUtils;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * PayPal支付策略实现 - 使用iJPay框架
 */
@Service("paypalPaymentStrategy")
public class PayPalPaymentStrategy implements PaymentStrategy {
    
    private static final Logger log = LoggerFactory.getLogger(PayPalPaymentStrategy.class);
    
    @Autowired
    private PayPalConfig payPalConfig;
    
    @Autowired
    private PaymentOrderMapper orderMapper;
    
    @Autowired
    private IPaymentOrderService paymentOrderService;
    


    /**
     * 获取PayPal配置
     */
    private PayPalApiConfig getPayPalApiConfig() {
        PayPalApiConfig apiConfig = new PayPalApiConfig();
        apiConfig.setClientId(payPalConfig.getClientId());
        apiConfig.setSecret(payPalConfig.getClientSecret());
        apiConfig.setSandBox(payPalConfig.isSandbox());
        return apiConfig;
    }

    /**
     * 处理支付
     *     1.创建PayPal订单,返回paypal订单ID和批准URL,前端跳转到批准URL进行支付
     *     2.更新套餐订单信息:关联paypal订单ID,支付方式
     * order:套餐订单信息(注意与paypal订单不同,里面字段paymentid关联paypal订单ID)
     */
    @Override
    public PaymentResponse processPayment(PaymentOrder order) {
        try {
            // 创建PayPal订单
            String result = createPayPalOrder(order);
            
            if (StrUtil.isBlank(result)) {
                log.error("PayPal创建订单失败");
                return new PaymentResponse(false, "PayPal创建订单失败", null);
            }
            
            // 解析响应
            JSONObject jsonObject = JSONUtil.parseObj(result);
            String id = jsonObject.getStr("id");
            String status = jsonObject.getStr("status");
            
            // 获取批准URL
            String approvalUrl = "";
            if (jsonObject.containsKey("links")) {
                for (Object link : jsonObject.getJSONArray("links")) {
                    JSONObject linkObj = JSONUtil.parseObj(link);
                    if ("approve".equals(linkObj.getStr("rel"))) {
                        approvalUrl = linkObj.getStr("href");
                        break;
                    }
                }
            }
            
            // 更新订单中的PayPal支付ID
            order.setPaymentId(id);
            order.setPaymentMethod("paypal");
            orderMapper.updatePaymentOrder(order);
            
            // 构建响应数据
            Map<String, Object> data = new HashMap<>();
            data.put("paymentId", id);
            data.put("paymentMethod ", "paypal");
            data.put("approvalUrl", approvalUrl);
            
            return new PaymentResponse(true, "创建PayPal订单成功", data);
        } catch (Exception e) {
            log.error("处理PayPal支付异常", e);
            return new PaymentResponse(false, "处理PayPal支付异常: " + e.getMessage(), null);
        }
    }
    
   
    /**
     * 处理买家授权支付成功后,PayPal回调(买家授权支付成功状态是APPROVED,还需执行捕获操作,完成支付)
     *     1.查询PayPal订单状态
     *     2.如果状态是APPROVED,则执行捕获操作,完成支付
     *     3.更新套餐订单信息:关联paypal订单ID,支付状态,支付方式,支付时间,支付金额等
     *     4.返回支付结果
     * params:PayPal订单ID和支付者ID
     *        params.get("token") paypal订单ID
     *        params.get("PayerID") paypal支付者ID
     */
    @Override
    public PaymentResponse handleCallbackSuccess(Map<String, String> params) {
        try {
            // 获取PayPal订单ID和支付者ID
            String payPalOrderId = params.get("token");
            String payerId = params.get("PayerID");
            
            if (StrUtil.isBlank(payPalOrderId) || StrUtil.isBlank(payerId)) {
                return new PaymentResponse(false, "回调参数中缺少必要参数", null);
            }
            
            // 查询订单
            String result = queryOrder(payPalOrderId);
            if (StrUtil.isBlank(result)) {
                return new PaymentResponse(false, "获取PayPal订单失败", null);
            }
            
            JSONObject orderJson = JSONUtil.parseObj(result);
            String status = orderJson.getStr("status");
            String payer = orderJson.getStr("payer");
            String orderNo = orderJson.getJSONArray("purchase_units").getJSONObject(0).getStr("reference_id");
            String payer_email = orderJson.getJSONObject("payer").getStr("email_address");
            //String payer_id    = captureJson.getJSONObject("payer").getStr("payer_id");

            PaymentOrder paymentOrder = null;  
            // 如果状态是APPROVED，执行capture操作 ,在webhook回调操作,这里不执行
            if ("APPROVED".equalsIgnoreCase(status)) {
                 log.info("订单已授权，开始执行capture操作，orderId: {}", payPalOrderId);
                 String captureResult = captureOrder(payPalOrderId);
                 
                 if (StrUtil.isBlank(captureResult)) {
                     return new PaymentResponse(false, "捕获PayPal订单失败", null);
                 }
                 
                 // 更新订单状态
                 JSONObject captureJson = JSONUtil.parseObj(captureResult);
                 status = captureJson.getStr("status");
                 log.info("Capture完成，最终状态: {}", status);
                 log.info("Capture完成，最终captureJson: {}", captureJson);
             
                 
                // 处理支付成功,更新套餐订单信息:关联paypal订单ID,支付状态,支付方式,支付时间,支付金额等
                 paymentOrder=orderMapper.selectPaymentOrderByOrderNo(orderNo);
                 if("0".equals(paymentOrder.getStatus())){//如果订单状态为0待支付,则更新订单状态 订单状态（0=待支付, 1=已完成, 2=已取消, 3=支付失败）
                    paymentOrder.setPaymentId(payPalOrderId);
                    paymentOrder.setPaymentMethod("paypal");
                    paymentOrder.setPayerId(payer_email);
                    paymentOrderService.processPaymentSuccess(paymentOrder);
                 }
            }

            if(paymentOrder==null){paymentOrder=orderMapper.selectPaymentOrderByOrderNo(orderNo);}
            // 构建返回数据
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("paymentId", payPalOrderId);
            resultMap.put("payerId", payerId);
            resultMap.put("payer_email", payer_email);
            resultMap.put("paymentMethod", "paypal");
            resultMap.put("status", status);
            resultMap.put("packageName", paymentOrder.getPackageName());
            resultMap.put("amount", paymentOrder.getAmount().toString());
            resultMap.put("currency", paymentOrder.getCurrency());
            
            return new PaymentResponse(true, "处理回调成功", resultMap);
        } catch (Exception e) {
            log.error("处理PayPal回调异常", e);
            return new PaymentResponse(false, "处理PayPal回调异常: " + e.getMessage(), null);
        }
    }
    
    @Override
    public PaymentResponse handleCallbackSuccess(HttpServletRequest request) {
        String payPalOrderId = request.getParameter("token");
        String payerId = request.getParameter("PayerID");

        Map<String, String> params = new HashMap<>();
        params.put("token", payPalOrderId);
        params.put("PayerID", payerId);
        try {
            return handleCallbackSuccess(params);
        } catch (Exception e) {
            log.error("处理PayPal回调异常", e);
            return new PaymentResponse(false, "处理PayPal回调异常: " + e.getMessage(), null);
        }
    }


     /**
     * 处理买家取消支付后,PayPal回调
     * params:PayPal订单ID
     *        params.get("token") paypal订单ID     
     * return 返回PaymentOrder订单orderNo
     */ 
    @Override
    public PaymentResponse handleCallbackCancel(Map<String, String> params) {
        // 获取PayPal订单ID和支付者ID
        String payPalOrderId = params.get("token");
        
        if (StrUtil.isBlank(payPalOrderId)) {
            return new PaymentResponse(false, "回调参数中缺少必要参数", null);
        }
        
        // 查询订单状态
        String result = queryOrder(payPalOrderId);
        if (StrUtil.isBlank(result)) {
            return new PaymentResponse(false, "获取PayPal订单失败", null);
        }
        
        JSONObject orderJson = JSONUtil.parseObj(result);
        //String status = orderJson.getStr("status");
        String orderNo = orderJson.getJSONArray("purchase_units").getJSONObject(0).getStr("reference_id");

        //PaymentOrder paymentOrder = orderMapper.selectPaymentOrderByOrderNo(orderNo);

        // 构建返回数据
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("paymentId", payPalOrderId);
        resultMap.put("orderNo", orderNo);
        return new PaymentResponse(true, "处理回调成功", resultMap);
    }
    
   
    @Override
    public PaymentResult completePayment(Map<String, Object> params) {
        try {
            // 获取PayPal订单ID
            String payPalOrderId = (String) params.get("token");
            if (StrUtil.isBlank(payPalOrderId)) {
                return PaymentResult.fail("INVALID_PARAMS", "参数中缺少PayPal订单ID");
            }
            
            // 执行捕获（完成支付）
            String result = captureOrder(payPalOrderId);
            if (StrUtil.isBlank(result)) {
                return PaymentResult.fail("PAYPAL_CAPTURE_ERROR", "捕获PayPal订单失败");
            }
            
            // 解析捕获响应
            JSONObject captureJson = JSONUtil.parseObj(result);
            String status = captureJson.getStr("status");
            String captureId = "";
            
            if (captureJson.containsKey("purchase_units")) {
                JSONObject firstUnit = captureJson.getJSONArray("purchase_units").getJSONObject(0);
                if (firstUnit.containsKey("payments") && firstUnit.getJSONObject("payments").containsKey("captures")) {
                    JSONObject capture = firstUnit.getJSONObject("payments").getJSONArray("captures").getJSONObject(0);
                    captureId = capture.getStr("id");
                }
            }
            
            // 构建返回数据
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("paymentId", payPalOrderId);
            resultMap.put("captureId", captureId);
            resultMap.put("status", status);
            
            return PaymentResult.success(payPalOrderId, mapPayPalStatus(status), resultMap);
        } catch (Exception e) {
            log.error("完成PayPal支付异常", e);
            return PaymentResult.fail("PAYPAL_COMPLETE_ERROR", "完成PayPal支付异常: " + e.getMessage());
        }
    }
    
    public String completePayment(String payPalOrderId) {
        try {
            if (StrUtil.isBlank(payPalOrderId)) {
                //return PaymentResult.fail("INVALID_PARAMS", "参数中缺少PayPal订单ID");
                return null;
            } 
            // 执行捕获（完成支付）
            String result = captureOrder(payPalOrderId);
            return result;

            // 解析捕获响应
            // JSONObject captureJson = JSONUtil.parseObj(result);
            // String status = captureJson.getStr("status");
            // String captureId = "";
            // 
            // if (captureJson.containsKey("purchase_units")) {
            //     JSONObject firstUnit = captureJson.getJSONArray("purchase_units").getJSONObject(0);
            //     if (firstUnit.containsKey("payments") && firstUnit.getJSONObject("payments").containsKey("captures")) {
            //         JSONObject capture = firstUnit.getJSONObject("payments").getJSONArray("captures").getJSONObject(0);
            //         captureId = capture.getStr("id");
            //     }
            // }
            
            //// 构建返回数据
            //Map<String, Object> resultMap = new HashMap<>();
            //resultMap.put("paymentId", payPalOrderId);
            //resultMap.put("captureId", captureId);
            //resultMap.put("status", status);
            //
            //return PaymentResult.success(payPalOrderId, mapPayPalStatus(status), resultMap);
        } catch (Exception e) {
            log.error("完成PayPal支付异常", e);
            return null;
            //return PaymentResult.fail("PAYPAL_COMPLETE_ERROR", "完成PayPal支付异常: " + e.getMessage());
        }
    }

    @Override
    public PaymentResult createPayment(Map<String, Object> params) {
        try {
            // 获取参数
            String orderId = (String) params.get("orderId");
            BigDecimal amount = (BigDecimal) params.get("amount");
            String description = (String) params.get("description");
            String currency = params.containsKey("currency") ? (String) params.get("currency") : "USD";
            
            // 创建订单数据
            PaymentOrder order = new PaymentOrder();
            order.setOrderNo(orderId);
            order.setAmount(amount);
            order.setCurrency(currency);
            order.setDescription(description);
            
            // 创建PayPal订单
            String result = createPayPalOrder(order);
            
            if (StrUtil.isBlank(result)) {
                log.error("PayPal创建订单失败");
                return PaymentResult.fail("PAYPAL_CREATE_ERROR", "PayPal创建订单失败");
            }
            
            // 解析响应
            JSONObject jsonObject = JSONUtil.parseObj(result);
            String id = jsonObject.getStr("id");
            String status = jsonObject.getStr("status");
            
            // 获取批准URL
            String approvalUrl = "";
            if (jsonObject.containsKey("links")) {
                for (Object link : jsonObject.getJSONArray("links")) {
                    JSONObject linkObj = JSONUtil.parseObj(link);
                    if ("approve".equals(linkObj.getStr("rel"))) {
                        approvalUrl = linkObj.getStr("href");
                        break;
                    }
                }
            }
            
            // 更新订单中的PayPal支付ID
            order.setPaymentId(id);
            order.setPaymentChannel("paypal");
            orderMapper.updatePaymentOrder(order);
            
            // 构建返回数据
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("paymentId", id);
            resultMap.put("approvalUrl", approvalUrl);
            resultMap.put("status", status);
            
            return PaymentResult.success(id, mapPayPalStatus(status), resultMap)
                    .setRedirectUrl(approvalUrl);
        } catch (Exception e) {
            log.error("创建PayPal支付异常", e);
            return PaymentResult.fail("PAYPAL_CREATE_ERROR", "创建PayPal支付异常: " + e.getMessage());
        }
    }
    
    @Override
    public PaymentResult executePayment(Map<String, Object> params) {
        try {
            // 获取PayPal订单ID
            String payPalOrderId = (String) params.get("token");
            if (StrUtil.isBlank(payPalOrderId)) {
                return PaymentResult.fail("INVALID_PARAMS", "参数中缺少PayPal订单ID");
            }
            
            // 执行捕获（完成支付）
            String result = captureOrder(payPalOrderId);
            if (StrUtil.isBlank(result)) {
                return PaymentResult.fail("PAYPAL_EXECUTE_ERROR", "执行PayPal支付失败");
            }
            
            // 解析捕获响应
            JSONObject captureJson = JSONUtil.parseObj(result);
            String status = captureJson.getStr("status");
            
            // 构建返回数据
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("paymentId", payPalOrderId);
            resultMap.put("status", status);
            
            return PaymentResult.success(payPalOrderId, mapPayPalStatus(status), resultMap);
        } catch (Exception e) {
            log.error("执行PayPal支付异常", e);
            return PaymentResult.fail("PAYPAL_EXECUTE_ERROR", "执行PayPal支付异常: " + e.getMessage());
        }
    }
    

    @Override
    public PaymentResult queryPaymentStatus(String orderNo) {
        PaymentOrder order = orderMapper.selectPaymentOrderByOrderNo(orderNo);
        if (order == null) {
            return PaymentResult.fail("ORDER_NOT_FOUND", "未找到订单");
        }    
        // 获取PayPal订单ID
        String payPalOrderId = order.getPaymentId();
        if (StrUtil.isBlank(payPalOrderId)) {
            return PaymentResult.fail("PAYPAL_ORDER_NOT_FOUND", "订单未关联PayPal订单");
        }
        return getPaymentStatus(payPalOrderId);
       
    }
    
    @Override
    public PaymentResult getPaymentStatus(Map<String, Object> params) {
        return getPaymentStatus((String) params.get("token"));    
    }

    @Override
    public PaymentResult getPaymentStatus(String paymentId) {
        try {
            // 获取PayPal订单ID
            String payPalOrderId = paymentId;
            if (StrUtil.isBlank(payPalOrderId)) {
                return PaymentResult.fail("INVALID_PARAMS", "参数中缺少PayPal订单ID");
            }
            
            // 查询PayPal订单状态
            String result = queryOrder(payPalOrderId);
            if (StrUtil.isBlank(result)) {
                return PaymentResult.fail("PAYPAL_STATUS_ERROR", "查询PayPal订单状态失败");
            }
            
            // 解析响应
            JSONObject orderJson = JSONUtil.parseObj(result);
            String status = orderJson.getStr("status");
            
            // 构建返回数据
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("paymentId", payPalOrderId);
            resultMap.put("status", status);
            
            return PaymentResult.success(payPalOrderId, mapPayPalStatus(status), resultMap);
        } catch (Exception e) {
            log.error("查询PayPal支付状态异常", e);
            return PaymentResult.fail("PAYPAL_STATUS_ERROR", "查询PayPal支付状态异常: " + e.getMessage());
        }
    }
    
   
    
    /**
     * 创建PayPal订单
     */
    private String createPayPalOrder(PaymentOrder order) {
        try {
            // 初始化PayPal API配置
            PayPalApiConfigKit.setThreadLocalApiConfig(getPayPalApiConfig());
            
            //参数请求参数文档 https://developer.paypal.com/docs/api/orders/v2/#orders_create
            // 构建订单请求体
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("intent", "CAPTURE");
            
            // 购买单元
            List<Map<String, Object>> purchaseUnits = new ArrayList<>();
            Map<String, Object> purchaseUnit = new HashMap<>();
            purchaseUnit.put("reference_id", order.getOrderNo());
            purchaseUnit.put("description", order.getDescription());
            
            // 金额
            Map<String, Object> amount = new HashMap<>();
            amount.put("currency_code", order.getCurrency() != null ? order.getCurrency() : "USD");
            amount.put("value", order.getAmount().toString());
            purchaseUnit.put("amount", amount);
            
            purchaseUnits.add(purchaseUnit);
            requestMap.put("purchase_units", purchaseUnits);
            
            // 应用上下文
            Map<String, Object> appContext = new HashMap<>();
            appContext.put("return_url", payPalConfig.getSuccessUrl());
            appContext.put("cancel_url", payPalConfig.getCancelUrl());
            appContext.put("user_action", "PAY_NOW");
            requestMap.put("application_context", appContext);
            
            // 转换为JSON
            String reqBody = JSONUtil.toJsonStr(requestMap);
            log.info("创建PayPal订单请求参数reqBody: {}", reqBody);
            // 发送请求
            String url = PayPalApiConfigKit.getApiConfig().isSandBox() 
                    ? PayPalApiUrl.SANDBOX_ORDERS_URL : PayPalApiUrl.LIVE_ORDERS_URL;
            //PayPalApi.createOrder(reqBody);
            // 获取访问令牌
            String accessToken = getAccessToken();
            if (StrUtil.isBlank(accessToken)) {
                log.error("获取PayPal访问令牌失败");
                return null;
            }
            
            // 设置请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + accessToken);
            
            // 发送请求
            String result = HttpUtils.post(url, reqBody, headers);
            log.info("创建PayPal订单响应: {}", result);
            
            return result;
        } catch (Exception e) {
            log.error("创建PayPal订单异常", e);
            return null;
        } finally {
            PayPalApiConfigKit.removeThreadLocalApiConfig();
        }
    }
    
    /**
     * 获取PayPal访问令牌
     */
    private String getAccessToken() {
        try {
            PayPalApiConfig apiConfig = PayPalApiConfigKit.getApiConfig();
            String url = apiConfig.isSandBox() ? 
                    PayPalApiUrl.SANDBOX_ACCESS_TOKEN_URL : PayPalApiUrl.LIVE_ACCESS_TOKEN_URL;
            
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            headers.put("Accept-Language", "en_US");
            headers.put("Content-Type", "application/x-www-form-urlencoded");
            
            String authorization = PayKit.base64Encode(apiConfig.getClientId() + ":" + apiConfig.getSecret());
            headers.put("Authorization", "Basic " + authorization);
            
            Map<String, Object> params = new HashMap<>();
            params.put("grant_type", "client_credentials");
            
            String result = HttpUtils.post(url, PayKit.createLinkString(params, true), headers);
            //log.info("获取PayPal访问令牌响应: {}", result);
            
            JSONObject json = JSONUtil.parseObj(result);
            return json.getStr("access_token");
        } catch (Exception e) {
            log.error("获取PayPal访问令牌异常", e);
            return null;
        }
    }
    
    /**
     * 获取PayPal订单
     */
    private String queryOrder(String orderId) {
        try {
            // 初始化PayPal API配置
            PayPalApiConfigKit.setThreadLocalApiConfig(getPayPalApiConfig());
            
            // 获取访问令牌
            String accessToken = getAccessToken();
            if (StrUtil.isBlank(accessToken)) {
                log.error("获取PayPal访问令牌失败");
                return null;
            }
            
            // 设置请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + accessToken);
            
            // 发送请求
            String url = PayPalApiConfigKit.getApiConfig().isSandBox() 
                    ? PayPalApiUrl.SANDBOX_ORDERS_URL : PayPalApiUrl.LIVE_ORDERS_URL;
            url += "/" + orderId;
            
            String result = HttpUtils.get(url, null, headers);
            log.info("获取PayPal订单响应: {}", result);
            System.out.println("获取PayPal订单响应: " + result);
            return result;
        } catch (Exception e) {
            log.error("获取PayPal订单异常", e);
            return null;
        } finally {
            PayPalApiConfigKit.removeThreadLocalApiConfig();
        }
    }
    
    /**
     * 捕获PayPal订单（完成支付）
     */
    private String captureOrder(String orderId) {
        try {
            // 初始化PayPal API配置
            PayPalApiConfigKit.setThreadLocalApiConfig(getPayPalApiConfig());
            
            // 获取访问令牌
            String accessToken = getAccessToken();
            if (StrUtil.isBlank(accessToken)) {
                log.error("获取PayPal访问令牌失败");
                return null;
            }
            //PayPalApi.captureOrder(getPayPalApiConfig(), orderId, "");
            // 设置请求头
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + accessToken);
            
            // 发送请求
            String url = PayPalApiConfigKit.getApiConfig().isSandBox() 
                    ? PayPalApiUrl.SANDBOX_ORDERS_URL : PayPalApiUrl.LIVE_ORDERS_URL;
            url += "/" + orderId + "/capture";
            
            String result = HttpUtils.post(url, "{}", headers);
            log.info("捕获PayPal订单响应: {}", result);
            
            return result;
        } catch (Exception e) {
            log.error("捕获PayPal订单异常", e);
            return null;
        } finally {
            PayPalApiConfigKit.removeThreadLocalApiConfig();
        }
    }
    
    /**
     * 将PayPal状态映射为系统状态
     */
    private String mapPayPalStatus(String paypalStatus) {
        switch (paypalStatus.toLowerCase()) {
            case "created":
                return "PENDING";
            case "approved":
            case "payer_action_required":
                return "APPROVED";
            case "completed":
            case "captured":
                return "PAID";
            case "voided":
            case "declined":
            case "failed":
                return "FAILED";
            default:
                return "PROCESSING";
        }
    }
} 