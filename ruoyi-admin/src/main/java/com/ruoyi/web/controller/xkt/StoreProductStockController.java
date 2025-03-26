package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreProductStock;
import com.ruoyi.xkt.service.IStoreProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口商品库存Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/prod-stocks")
public class StoreProductStockController extends BaseController {
    @Autowired
    private IStoreProductStockService storeProductStockService;

    /**
     * 查询档口商品库存列表
     */
    @PreAuthorize("@ss.hasPermi('system:stock:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductStock storeProductStock) {
        startPage();
        List<StoreProductStock> list = storeProductStockService.selectStoreProductStockList(storeProductStock);
        return getDataTable(list);
    }

    /**
     * 导出档口商品库存列表
     */
    @PreAuthorize("@ss.hasPermi('system:stock:export')")
    @Log(title = "档口商品库存", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductStock storeProductStock) {
        List<StoreProductStock> list = storeProductStockService.selectStoreProductStockList(storeProductStock);
        ExcelUtil<StoreProductStock> util = new ExcelUtil<StoreProductStock>(StoreProductStock.class);
        util.exportExcel(response, list, "档口商品库存数据");
    }

    /**
     * 获取档口商品库存详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:stock:query')")
    @GetMapping(value = "/{storeProdStockId}")
    public AjaxResult getInfo(@PathVariable("storeProdStockId") Long storeProdStockId) {
        return success(storeProductStockService.selectStoreProductStockByStoreProdStockId(storeProdStockId));
    }

    /**
     * 新增档口商品库存
     */
    @PreAuthorize("@ss.hasPermi('system:stock:add')")
    @Log(title = "档口商品库存", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoreProductStock storeProductStock) {
        return toAjax(storeProductStockService.insertStoreProductStock(storeProductStock));
    }

    /**
     * 修改档口商品库存
     */
    @PreAuthorize("@ss.hasPermi('system:stock:edit')")
    @Log(title = "档口商品库存", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoreProductStock storeProductStock) {
        return toAjax(storeProductStockService.updateStoreProductStock(storeProductStock));
    }

    /**
     * 删除档口商品库存
     */
    @PreAuthorize("@ss.hasPermi('system:stock:remove')")
    @Log(title = "档口商品库存", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdStockIds}")
    public AjaxResult remove(@PathVariable Long[] storeProdStockIds) {
        return toAjax(storeProductStockService.deleteStoreProductStockByStoreProdStockIds(storeProdStockIds));
    }
}
