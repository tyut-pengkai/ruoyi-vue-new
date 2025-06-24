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
import com.ruoyi.payment.domain.PaymentPackage;
import com.ruoyi.payment.service.IPaymentPackageService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 商品套餐Controller
 * 
 * @author auto
 * @date 2025-06-24
 */
@RestController
@RequestMapping("/payment/package")
public class PaymentPackageController extends BaseController
{
    @Autowired
    private IPaymentPackageService paymentPackageService;

    /**
     * 查询可用的商品套餐列表（供用户选择）
     */
    @GetMapping("/listForUser")
    public TableDataInfo listForUser()
    {
        List<PaymentPackage> list = paymentPackageService.selectAvailablePackageList();
        return getDataTable(list);
    }

    /**
     * 查询商品套餐列表
     */
    @PreAuthorize("@ss.hasPermi('payment:package:list')")
    @GetMapping("/list")
    public TableDataInfo list(PaymentPackage paymentPackage)
    {
        startPage();
        List<PaymentPackage> list = paymentPackageService.selectPaymentPackageList(paymentPackage);
        return getDataTable(list);
    }

    /**
     * 导出商品套餐列表
     */
    @PreAuthorize("@ss.hasPermi('payment:package:export')")
    @Log(title = "商品套餐", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PaymentPackage paymentPackage)
    {
        List<PaymentPackage> list = paymentPackageService.selectPaymentPackageList(paymentPackage);
        ExcelUtil<PaymentPackage> util = new ExcelUtil<PaymentPackage>(PaymentPackage.class);
        util.exportExcel(response, list, "商品套餐数据");
    }

    /**
     * 获取商品套餐详细信息
     */
    @PreAuthorize("@ss.hasPermi('payment:package:query')")
    @GetMapping(value = "/{packageId}")
    public AjaxResult getInfo(@PathVariable("packageId") Long packageId)
    {
        return success(paymentPackageService.selectPaymentPackageByPackageId(packageId));
    }

    /**
     * 新增商品套餐
     */
    @PreAuthorize("@ss.hasPermi('payment:package:add')")
    @Log(title = "商品套餐", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PaymentPackage paymentPackage)
    {
        return toAjax(paymentPackageService.insertPaymentPackage(paymentPackage));
    }

    /**
     * 修改商品套餐
     */
    @PreAuthorize("@ss.hasPermi('payment:package:edit')")
    @Log(title = "商品套餐", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PaymentPackage paymentPackage)
    {
        return toAjax(paymentPackageService.updatePaymentPackage(paymentPackage));
    }

    /**
     * 删除商品套餐
     */
    @PreAuthorize("@ss.hasPermi('payment:package:remove')")
    @Log(title = "商品套餐", businessType = BusinessType.DELETE)
	@DeleteMapping("/{packageIds}")
    public AjaxResult remove(@PathVariable Long[] packageIds)
    {
        return toAjax(paymentPackageService.deletePaymentPackageByPackageIds(packageIds));
    }
}
