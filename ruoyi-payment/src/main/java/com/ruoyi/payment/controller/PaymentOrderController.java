package com.ruoyi.payment.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.payment.domain.PaymentOrder;
import com.ruoyi.payment.domain.dto.CreateOrderRequest;
import com.ruoyi.payment.service.IPaymentOrderService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;

import java.util.Map;

/**
 * 支付订单Controller
 * 
 * @author auto
 * @date 2025-06-24
 */
@RestController
@RequestMapping("/payment/order")
public class PaymentOrderController extends BaseController
{
    @Autowired
    private IPaymentOrderService paymentOrderService;

    /**
     * 查询支付订单列表
     */
    @PreAuthorize("@ss.hasPermi('payment:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(PaymentOrder paymentOrder)
    {
        startPage();
        // 如果不是管理员，则只查询自己的订单
        if (!SecurityUtils.isAdmin(getUserId()))
        {
            paymentOrder.setUserId(getUserId());
        }
        List<PaymentOrder> list = paymentOrderService.selectPaymentOrderList(paymentOrder);
        return getDataTable(list);
    }

    /**
     * 导出支付订单列表
     */
    @PreAuthorize("@ss.hasPermi('payment:order:export')")
    @Log(title = "支付订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PaymentOrder paymentOrder)
    {
        List<PaymentOrder> list = paymentOrderService.selectPaymentOrderList(paymentOrder);
        ExcelUtil<PaymentOrder> util = new ExcelUtil<PaymentOrder>(PaymentOrder.class);
        util.exportExcel(response, list, "支付订单数据");
    }

    /**
     * 获取支付订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:order:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId)
    {
        return success(paymentOrderService.selectPaymentOrderByOrderId(orderId));
    }

    /**
     * 新增支付订单
     */
    @PreAuthorize("@ss.hasPermi('payment:order:add')")
    @Log(title = "支付订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PaymentOrder paymentOrder)
    {
        return toAjax(paymentOrderService.insertPaymentOrder(paymentOrder));
    }

    /**
     * 修改支付订单
     */
    @PreAuthorize("@ss.hasPermi('payment:order:edit')")
    @Log(title = "支付订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PaymentOrder paymentOrder)
    {
        return toAjax(paymentOrderService.updatePaymentOrder(paymentOrder));
    }

    /**
     * 删除支付订单
     */
    @PreAuthorize("@ss.hasPermi('payment:order:remove')")
    @Log(title = "支付订单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds)
    {
        return toAjax(paymentOrderService.deletePaymentOrderByOrderIds(orderIds));
    }

    /**
     * 用户创建订单
     */
    @Log(title = "用户创建订单", businessType = BusinessType.INSERT)
    @PostMapping("/create")
    public AjaxResult create(@RequestBody CreateOrderRequest request)
    {
        return AjaxResult.success(paymentOrderService.createOrder(request));
    }

    /**
     * 取消支付订单
     */
    @Log(title = "取消支付订单", businessType = BusinessType.UPDATE)
    @PostMapping("/cancel/{orderId}")
    public AjaxResult cancel(@PathVariable Long orderId)
    {
        return toAjax(paymentOrderService.cancelOrder(orderId));
    }

    /**
     * 模拟支付成功回调
     */
    @Log(title = "模拟支付", businessType = BusinessType.UPDATE)
    @PostMapping("/mock-pay/{orderNo}")
    public AjaxResult mockPay(@PathVariable String orderNo)
    {
        paymentOrderService.processPaymentSuccess(orderNo);
        return AjaxResult.success("模拟支付成功");
    }
}
