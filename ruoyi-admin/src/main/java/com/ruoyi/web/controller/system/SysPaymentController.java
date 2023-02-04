package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.payment.constants.PaymentDefine;
import com.ruoyi.system.domain.SysPayment;
import com.ruoyi.system.service.ISysPaymentService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 支付配置Controller
 *
 * @author zwgu
 * @date 2022-03-24
 */
@RestController
@RequestMapping("/system/payment")
public class SysPaymentController extends BaseController {
    @Autowired
    private ISysPaymentService sysPaymentService;

    /**
     * 查询支付配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:payment:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPayment sysPayment) {
        startPage();
        List<SysPayment> list = sysPaymentService.selectSysPaymentList(sysPayment);
        return getDataTable(list);
    }

    /**
     * 导出支付配置列表
     */
    @PreAuthorize("@ss.hasPermi('system:payment:export')")
    @Log(title = "支付配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPayment sysPayment) {
        List<SysPayment> list = sysPaymentService.selectSysPaymentList(sysPayment);
        ExcelUtil<SysPayment> util = new ExcelUtil<SysPayment>(SysPayment.class);
        util.exportExcel(response, list, "支付配置数据");
    }

    /**
     * 获取支付配置详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:payment:query')")
    @GetMapping(value = "/{payId}")
    public AjaxResult getInfo(@PathVariable("payId") Long payId) {
        return AjaxResult.success(sysPaymentService.selectSysPaymentByPayId(payId));
    }

    /**
     * 新增支付配置
     */
    @PreAuthorize("@ss.hasPermi('system:payment:add')")
    @Log(title = "支付配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysPayment sysPayment) {
        sysPayment.setCreateBy(getUsername());
        int payment = sysPaymentService.insertSysPayment(sysPayment);
        PaymentDefine.reloadPayment();
        return toAjax(payment);
    }

    /**
     * 修改支付配置
     */
    @PreAuthorize("@ss.hasPermi('system:payment:edit')")
    @Log(title = "支付配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysPayment sysPayment) {
        sysPayment.setUpdateBy(getUsername());
        int payment = sysPaymentService.updateSysPayment(sysPayment);
        PaymentDefine.reloadPayment();
        return toAjax(payment);
    }

    /**
     * 删除支付配置
     */
    @PreAuthorize("@ss.hasPermi('system:payment:remove')")
    @Log(title = "支付配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{payIds}")
    public AjaxResult remove(@PathVariable Long[] payIds) {
        int payment = sysPaymentService.deleteSysPaymentByPayIds(payIds);
        PaymentDefine.reloadPayment();
        return toAjax(payment);
    }
}
