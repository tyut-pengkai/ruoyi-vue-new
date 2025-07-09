package com.ruoyi.payment.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ruoyi.payment.service.IWebhookEventService;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.core.redis.RedisCache;

import com.ijpay.core.kit.RsaKit;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.payment.config.PayPalConfig;
import com.ruoyi.payment.domain.PaymentOrder;
import com.ruoyi.payment.mapper.PaymentOrderMapper;
import com.ruoyi.payment.service.IPaymentOrderService;
import com.ruoyi.payment.service.PayPalVerificationService;
import com.ruoyi.payment.strategy.PaymentStrategy;
import com.ruoyi.payment.strategy.PaymentStrategyFactory;
import com.ruoyi.payment.strategy.impl.PayPalPaymentStrategy;
import com.ruoyi.payment.util.PayPalApiConfigKit;
import com.ruoyi.common.utils.DateUtils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * PayPal Webhook控制器
 * 用于处理PayPal的异步通知
 */
@RestController
@RequestMapping("/payment/paypal/webhook")
public class PayPalWebhookController {
    private static final Logger log = LoggerFactory.getLogger(PayPalWebhookController.class);

    @Autowired
    private IWebhookEventService webhookEventService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private PayPalConfig payPalConfig;
    
    @Autowired
    private PaymentOrderMapper orderMapper;
    
    @Autowired
    private IPaymentOrderService paymentOrderService;

    @Autowired
    private PayPalVerificationService verificationService;

    @Autowired
    private PayPalPaymentStrategy payPalPaymentStrategy;


   /**
     * 处理PayPal Webhook通知
     *   1.防重复处理:redis缓存,数据库检查,订单状态判断
     *   2.验证 Webhook 签名（防伪造）
     *   3.执行业务逻辑
     * 
     * @param request HTTP请求
     * @param payload Webhook通知内容
     * @return 处理结果
     */
    @PostMapping
    public ResponseEntity<String> handleWebhook(HttpServletRequest request, @RequestBody String payload) {
        log.info("收到PayPal Webhook通知: {}", payload);
        
        try {
            JSONObject webhookEvent = JSONUtil.parseObj(payload);
            String eventId = webhookEvent.getStr("id");
            String summary = webhookEvent.getStr("summary");
            if (StrUtil.isBlank(eventId)) {
                log.warn("Webhook 事件ID为空，无法处理。");
                return ResponseEntity.badRequest().body("Event ID is missing.");
            }

            String redisKey = CacheConstants.PAYPAL_WEBHOOK_EVENT_KEY + eventId;

            // 1. 快速Redis检查
            if (redisCache.hasKey(redisKey)) {
                log.warn("Redis命中重复事件，事件ID: {}，已忽略。", eventId);
                return ResponseEntity.ok("Webhook event already processed.");
            }

            // 2. 数据库兜底检查
            if (webhookEventService.isEventProcessed(eventId)) {
                log.warn("数据库命中重复事件 (Redis缓存未命中)，事件ID: {}，已忽略。", eventId);
                // 修复缓存
                redisCache.setCacheObject(redisKey, "processed", 24, TimeUnit.HOURS);
                return ResponseEntity.ok("Webhook event already processed.");
            }

            // 3. 签名验证
            if (!verificationService.verifyWebhookSignature(request, payload)) {
                log.warn("Webhook 签名验证失败，请求被拒绝。");
                return ResponseEntity.status(403).body("Signature verification failed.");
            }
        
            // 4. 执行业务逻辑  // todo 需要异步处理,防止阻塞
            String eventType = webhookEvent.getStr("event_type");
            JSONObject resource = webhookEvent.getJSONObject("resource");
            log.info("PayPal Webhook事件类型: {}", eventType);
            
            switch (eventType) {
                case "CHECKOUT.ORDER.APPROVED":
                    log.info("An order has been approved by buyer订单已获得买方批准: {}", eventType);
                    handlePaymentORDERAPPROVED(resource);
                    break;
                case "PAYMENT.CAPTURE.COMPLETED": 
                    log.info("Payment completed for $ xx USD: {}", eventType);
                    handlePaymentCaptureCompleted(resource);
                    break;
                case "PAYMENT.CAPTURE.DENIED":
                    handlePaymentCaptureDenied(resource);
                    break;
                case "PAYMENT.CAPTURE.REFUNDED":
                    handlePaymentCaptureRefunded(resource);
                    break;
                
                default:
                    log.info("未处理的PayPal Webhook事件类型: {}", eventType);
                    break;
            }
            
            // 5. 持久化事件ID
            webhookEventService.recordEventProcessed(eventId, "paypal", eventType, summary);
            redisCache.setCacheObject(redisKey, "processed", 24, TimeUnit.HOURS);

            return ResponseEntity.ok("Webhook processed successfully");
        } catch (Exception e) {
            log.error("处理PayPal Webhook通知异常", e);
            return ResponseEntity.internalServerError().body("Error processing webhook");
        }
    }
    
     /**
     * 处理订单已获得买方批准事件, 支付捕获
     */
    private String handlePaymentORDERAPPROVED(JSONObject resource) {
        String payPalOrderId = resource.getStr("id");
        String payerEmail = resource.getJSONObject("payer").getStr("email_address");
        // 查找并更新订单
        PaymentOrder paymentOrder = paymentOrderService.selectPaymentOrderByPaymentId(payPalOrderId); 
        if (paymentOrder == null) {
            // 根据 paymentOrderNo 查询订单,然后更新 paymentOrder 订单的 支付方式, 支付者email, paymentid
            String paymentOrderNo = resource.getJSONArray("purchase_units").getJSONObject(0).getStr("reference_id");
            paymentOrder = paymentOrderService.getOrderByOrderNo(paymentOrderNo);
        }
        if(!"0".equals(paymentOrder.getStatus())){//（0=待支付, 1=已完成, 2=已取消, 3=支付失败）
            return "订单状态不为0，可能已处理，直接返回成功，防止重复处理";
        }
        // 更新 paymentOrder 订单的 支付方式, 支付者email
        paymentOrder.setPaymentMethod("paypal");
        paymentOrder.setPayerId(payerEmail);
        paymentOrder.setPaymentId(payPalOrderId);
        paymentOrderService.updatePaymentOrder(paymentOrder);
        return payPalPaymentStrategy.completePayment(payPalOrderId);       
    }


    /**
     * 处理支付捕获完成事件
     */
    private void handlePaymentCaptureCompleted(JSONObject resource) {
        try {
            String captureId = resource.getStr("id");
            String paymentId =  resource.getJSONObject("supplementary_data").getJSONObject("related_ids").getStr("order_id");
            String status = resource.getStr("status");
            //String payerEmail = resource.getJSONObject("payer").getStr("email_address");

            // 查找并更新订单
            PaymentOrder paymentOrder = paymentOrderService.selectPaymentOrderByPaymentId(paymentId); 
            if (paymentOrder != null) {
                paymentOrderService.processPaymentSuccess(paymentOrder);
                log.info("PayPal支付捕获完成，订单更新成功: {}", paymentId);
            } else {
                log.error("根据paypal订单id{}未找到对应的paymentOrder订单记录", paymentId);
            }
        } catch (Exception e) {
            log.error("处理支付捕获完成事件异常", e);
        }
    }
    
    /**
     * 处理支付捕获拒绝事件
     */
    private void handlePaymentCaptureDenied(JSONObject resource) {
        try {
            String captureId = resource.getStr("id");
            String orderId = resource.getStr("supplementary_data.related_ids.order_id");
            String status = resource.getStr("status");
            
            // 查找并更新订单状态
            //PaymentOrder order = orderMapper.selectPaymentOrderByPaymentId(orderId);
            PaymentOrder order = orderMapper.selectPaymentOrderByOrderId(Long.parseLong(orderId));
            if (order != null) {
                order.setStatus("FAILED");
                orderMapper.updatePaymentOrder(order);
                log.info("PayPal支付捕获被拒绝，订单状态已更新: {}", orderId);
            } else {
                log.error("未找到对应的订单记录: {}", orderId);
            }
        } catch (Exception e) {
            log.error("处理支付捕获拒绝事件异常", e);
        }
    }
    
    /**
     * 处理支付退款事件
     */
    private void handlePaymentCaptureRefunded(JSONObject resource) {
        try {
            String captureId = resource.getStr("id");
            String orderId = resource.getStr("supplementary_data.related_ids.order_id");
            String status = resource.getStr("status");
            
            // 查找并更新订单状态
            PaymentOrder order = orderMapper.selectPaymentOrderByPaymentId(orderId);
            if (order != null) {
                order.setStatus("REFUNDED");
                orderMapper.updatePaymentOrder(order);
                log.info("PayPal支付已退款，订单状态已更新: {}", orderId);
            } else {
                log.error("未找到对应的订单记录: {}", orderId);
            }
        } catch (Exception e) {
            log.error("处理支付退款事件异常", e);
        }
    }
} 