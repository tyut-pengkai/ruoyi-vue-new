package com.ruoyi.payment.controller;

import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.payment.model.PaymentResponse;
import com.ruoyi.payment.model.PaymentResult;
import com.ruoyi.payment.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;


import java.net.URI;
import java.util.Map;

/**
 * 支付API接口
 */
@RestController
@RequestMapping("/payment/api")
public class PaymentApiController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(PaymentApiController.class);
    
    
    @Autowired
    private PaymentService paymentService;

    @Value("${payment.successUrl}")
    private String successUrl;

    @Value("${payment.cancelUrl}")
    private String cancelUrl;
    
  
    
    /**
     * 处理买家授权支付成功后回调
     */
     @GetMapping("/callback/success/{paymentMethod}")
     public RedirectView handleCallbackSuccess(@PathVariable("paymentMethod") String paymentMethod,@RequestParam Map<String, String> params) {
         try {
             // 处理回调
             PaymentResponse response = paymentService.handleCallbackSuccess(paymentMethod,params);

             if (response.isSuccess()) {
                 // 支付成功，重定向到成功页面
                 UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(successUrl);
                 if (response.getData() instanceof Map) {
                     ((Map<String, ?>) response.getData()).forEach((key, value) -> {
                         if (value != null) {
                             urlBuilder.queryParam(key, String.valueOf(value));
                         }
                     });
                 }
                 return new RedirectView(urlBuilder.toUriString());
             } else {
                 // 支付失败，重定向到取消或失败页面
                 return new RedirectView(cancelUrl + "?message=" + response.getMessage());
             }
         } catch (Exception e) {
             log.error("处理支付回调失败", e);
             // 异常情况，重定向到错误页面
             return new RedirectView(cancelUrl + "?message=Error processing payment");
         }
     }

    /**
     * 处理买家取消支付后回调
     */
    @GetMapping("/callback/cancel/{paymentMethod}")
    public RedirectView handleCallbackCancel(@PathVariable("paymentMethod") String paymentMethod,@RequestParam Map<String, String> params) {
        try {
            // 处理回调
            PaymentResponse response = paymentService.handleCallbackCancel(paymentMethod,params);
            // 支付取消，重定向到取消页面
            UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromHttpUrl(cancelUrl);
            if (response.getData() instanceof Map) {
                ((Map<String, ?>) response.getData()).forEach((key, value) -> {
                    if (value != null) {
                        urlBuilder.queryParam(key, String.valueOf(value));
                    }
                });
            }
            return new RedirectView(urlBuilder.toUriString());

        } catch (Exception e) {
            log.error("处理支付回调失败", e);
            return new RedirectView(cancelUrl + "?message=Error processing cancellation");
        }
    }

    /**
     * 查询支付状态
     */
     @GetMapping("/status/{paymentId}/{paymentMethod}")
     public AjaxResult getPaymentStatus(@PathVariable("paymentId") String paymentId,@PathVariable("paymentMethod") String paymentMethod) {
         try {
             // 查询支付状态
             PaymentResult response = paymentService.getPaymentStatus(paymentId,paymentMethod);
             
             if (response.isSuccess()) {
                 return AjaxResult.success("查询支付状态成功", response.getData());
             } else {
                 return AjaxResult.error(response.getErrorMsg());
             }
         } catch (Exception e) {
             log.error("查询支付状态失败", e);
             return AjaxResult.error("查询支付状态失败: " + e.getMessage());
         }
     }
    
    /**
     * 创建支付订单并处理支付
     * 
     * @param request 创建订单请求
     * @param paymentMethod 支付方式（paypal）
     * @return 支付结果
     */
    // @PostMapping("/create/{paymentMethod}")
    // public AjaxResult createPayment(@RequestBody CreateOrderRequest request, @PathVariable String paymentMethod) {
    //     try {
    //         // 验证支付方式
    //         if (!"paypal".equals(paymentMethod)) {
    //             return AjaxResult.error("不支持的支付方式: " + paymentMethod);
    //         }
    //         
    //         // 1. 创建订单 并发起支付
    //         //PaymentResponse response = paymentService.initiatePayment(request.getOrderId(), paymentMethod);
    //         //if (!response.isSuccess()) {
    //         //    return AjaxResult.error(response.getMessage());
    //         //}
    //         
    //         // 2. 处理支付
    //         Map<String, Object> params = new HashMap<>();
    //         params.put("useCheckout", true); // 默认使用Checkout，也可由前端传递
    //         
    //         PaymentResult result = paymentService.processPayment(order.getOrderId(), paymentMethod, params);
    //         
    //         if (result.isSuccess()) {
    //             if (result.getRedirectUrl() != null) {
    //                 // 返回重定向URL
    //                 return AjaxResult.success("创建支付成功")
    //                         .put("redirectUrl", result.getRedirectUrl())
    //                         .put("orderId", order.getOrderId())
    //                         .put("transactionId", result.getTransactionId());
    //             } else {
    //                 // 直接支付成功或返回客户端所需信息
    //                 return AjaxResult.success("支付处理成功")
    //                         .put("orderId", order.getOrderId())
    //                         .put("transactionId", result.getTransactionId())
    //                         .put("data", result.getData());
    //             }
    //         } else {
    //             return AjaxResult.error(result.getErrorMsg());
    //         }
    //     } catch (Exception e) {
    //         log.error("创建支付订单异常", e);
    //         return AjaxResult.error("创建支付订单异常: " + e.getMessage());
    //     }
    // }

    /**
     * 发起支付
     */
    @Anonymous
    @PostMapping("/process")
    public AjaxResult pay(@RequestBody Map<String, String> params) {
        String orderNo = params.get("orderNo");
        String paymentMethod = params.get("paymentMethod");
        // 初始化创建订单并发起支付
        PaymentResponse response = paymentService.initiatePayment(orderNo, paymentMethod);
        if (!response.isSuccess()) {
            return AjaxResult.error(response.getMessage());
        }else{
            log.info("发起支付成功: {}", response.getData());
            return AjaxResult.success(response.getData());
        }

    }


   
} 