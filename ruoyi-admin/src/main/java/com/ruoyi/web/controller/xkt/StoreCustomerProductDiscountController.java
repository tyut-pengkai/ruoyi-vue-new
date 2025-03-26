package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreCustomerProductDiscount;
import com.ruoyi.xkt.service.IStoreCustomerProductDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口客户优惠Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/cus-discounts")
public class StoreCustomerProductDiscountController extends BaseController {
    @Autowired
    private IStoreCustomerProductDiscountService storeCustomerProductDiscountService;

    /**
     * 查询档口客户优惠列表
     */
    @PreAuthorize("@ss.hasPermi('system:discount:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreCustomerProductDiscount storeCustomerProductDiscount) {
        startPage();
        List<StoreCustomerProductDiscount> list = storeCustomerProductDiscountService.selectStoreCustomerProductDiscountList(storeCustomerProductDiscount);
        return getDataTable(list);
    }

    /**
     * 导出档口客户优惠列表
     */
    @PreAuthorize("@ss.hasPermi('system:discount:export')")
    @Log(title = "档口客户优惠", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreCustomerProductDiscount storeCustomerProductDiscount) {
        List<StoreCustomerProductDiscount> list = storeCustomerProductDiscountService.selectStoreCustomerProductDiscountList(storeCustomerProductDiscount);
        ExcelUtil<StoreCustomerProductDiscount> util = new ExcelUtil<StoreCustomerProductDiscount>(StoreCustomerProductDiscount.class);
        util.exportExcel(response, list, "档口客户优惠数据");
    }

    /**
     * 获取档口客户优惠详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:discount:query')")
    @GetMapping(value = "/{storeCusProdDiscId}")
    public AjaxResult getInfo(@PathVariable("storeCusProdDiscId") Long storeCusProdDiscId) {
        return success(storeCustomerProductDiscountService.selectStoreCustomerProductDiscountByStoreCusProdDiscId(storeCusProdDiscId));
    }

    /**
     * 新增档口客户优惠
     */
    @PreAuthorize("@ss.hasPermi('system:discount:add')")
    @Log(title = "档口客户优惠", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoreCustomerProductDiscount storeCustomerProductDiscount) {
        return toAjax(storeCustomerProductDiscountService.insertStoreCustomerProductDiscount(storeCustomerProductDiscount));
    }

    /**
     * 修改档口客户优惠
     */
    @PreAuthorize("@ss.hasPermi('system:discount:edit')")
    @Log(title = "档口客户优惠", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoreCustomerProductDiscount storeCustomerProductDiscount) {
        return toAjax(storeCustomerProductDiscountService.updateStoreCustomerProductDiscount(storeCustomerProductDiscount));
    }

    /**
     * 删除档口客户优惠
     */
    @PreAuthorize("@ss.hasPermi('system:discount:remove')")
    @Log(title = "档口客户优惠", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeCusProdDiscIds}")
    public AjaxResult remove(@PathVariable Long[] storeCusProdDiscIds) {
        return toAjax(storeCustomerProductDiscountService.deleteStoreCustomerProductDiscountByStoreCusProdDiscIds(storeCusProdDiscIds));
    }
}
