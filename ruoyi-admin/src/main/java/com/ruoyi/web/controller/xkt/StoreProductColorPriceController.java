package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreProductColorPrice;
import com.ruoyi.xkt.service.IStoreProductColorPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口商品颜色定价Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/prod-color-prices")
public class StoreProductColorPriceController extends BaseController {
    @Autowired
    private IStoreProductColorPriceService storeProductColorPriceService;

    /**
     * 查询档口商品颜色定价列表
     */
    @PreAuthorize("@ss.hasPermi('system:price:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductColorPrice storeProductColorPrice) {
        startPage();
        List<StoreProductColorPrice> list = storeProductColorPriceService.selectStoreProductColorPriceList(storeProductColorPrice);
        return getDataTable(list);
    }

    /**
     * 导出档口商品颜色定价列表
     */
    @PreAuthorize("@ss.hasPermi('system:price:export')")
    @Log(title = "档口商品颜色定价", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductColorPrice storeProductColorPrice) {
        List<StoreProductColorPrice> list = storeProductColorPriceService.selectStoreProductColorPriceList(storeProductColorPrice);
        ExcelUtil<StoreProductColorPrice> util = new ExcelUtil<StoreProductColorPrice>(StoreProductColorPrice.class);
        util.exportExcel(response, list, "档口商品颜色定价数据");
    }

    /**
     * 获取档口商品颜色定价详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:price:query')")
    @GetMapping(value = "/{storeProdColorPriceId}")
    public AjaxResult getInfo(@PathVariable("storeProdColorPriceId") Long storeProdColorPriceId) {
        return success(storeProductColorPriceService.selectStoreProductColorPriceByStoreProdColorPriceId(storeProdColorPriceId));
    }

    /**
     * 新增档口商品颜色定价
     */
    @PreAuthorize("@ss.hasPermi('system:price:add')")
    @Log(title = "档口商品颜色定价", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoreProductColorPrice storeProductColorPrice) {
        return toAjax(storeProductColorPriceService.insertStoreProductColorPrice(storeProductColorPrice));
    }

    /**
     * 修改档口商品颜色定价
     */
    @PreAuthorize("@ss.hasPermi('system:price:edit')")
    @Log(title = "档口商品颜色定价", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoreProductColorPrice storeProductColorPrice) {
        return toAjax(storeProductColorPriceService.updateStoreProductColorPrice(storeProductColorPrice));
    }

    /**
     * 删除档口商品颜色定价
     */
    @PreAuthorize("@ss.hasPermi('system:price:remove')")
    @Log(title = "档口商品颜色定价", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdColorPriceIds}")
    public AjaxResult remove(@PathVariable Long[] storeProdColorPriceIds) {
        return toAjax(storeProductColorPriceService.deleteStoreProductColorPriceByStoreProdColorPriceIds(storeProdColorPriceIds));
    }
}
