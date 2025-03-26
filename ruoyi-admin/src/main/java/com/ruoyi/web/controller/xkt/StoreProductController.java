package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreProduct;
import com.ruoyi.xkt.service.IStoreProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口商品Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/prods")
public class StoreProductController extends BaseController {
    @Autowired
    private IStoreProductService storeProductService;

    /**
     * 查询档口商品列表
     */
    @PreAuthorize("@ss.hasPermi('system:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProduct storeProduct) {
        startPage();
        List<StoreProduct> list = storeProductService.selectStoreProductList(storeProduct);
        return getDataTable(list);
    }

    /**
     * 导出档口商品列表
     */
    @PreAuthorize("@ss.hasPermi('system:product:export')")
    @Log(title = "档口商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProduct storeProduct) {
        List<StoreProduct> list = storeProductService.selectStoreProductList(storeProduct);
        ExcelUtil<StoreProduct> util = new ExcelUtil<StoreProduct>(StoreProduct.class);
        util.exportExcel(response, list, "档口商品数据");
    }

    /**
     * 获取档口商品详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:product:query')")
    @GetMapping(value = "/{storeProdId}")
    public AjaxResult getInfo(@PathVariable("storeProdId") Long storeProdId) {
        return success(storeProductService.selectStoreProductByStoreProdId(storeProdId));
    }

    /**
     * 新增档口商品
     */
    @PreAuthorize("@ss.hasPermi('system:product:add')")
    @Log(title = "档口商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoreProduct storeProduct) {
        return toAjax(storeProductService.insertStoreProduct(storeProduct));
    }

    /**
     * 修改档口商品
     */
    @PreAuthorize("@ss.hasPermi('system:product:edit')")
    @Log(title = "档口商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoreProduct storeProduct) {
        return toAjax(storeProductService.updateStoreProduct(storeProduct));
    }

    /**
     * 删除档口商品
     */
    @PreAuthorize("@ss.hasPermi('system:product:remove')")
    @Log(title = "档口商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdIds}")
    public AjaxResult remove(@PathVariable Long[] storeProdIds) {
        return toAjax(storeProductService.deleteStoreProductByStoreProdIds(storeProdIds));
    }
}
