package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreProductColorSize;
import com.ruoyi.xkt.service.IStoreProductColorSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口商品颜色的尺码Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/prod-color-sizes")
public class StoreProductColorSizeController extends BaseController {
    @Autowired
    private IStoreProductColorSizeService storeProductColorSizeService;

    /**
     * 查询档口商品颜色的尺码列表
     */
    @PreAuthorize("@ss.hasPermi('system:size:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductColorSize storeProductColorSize) {
        startPage();
        List<StoreProductColorSize> list = storeProductColorSizeService.selectStoreProductColorSizeList(storeProductColorSize);
        return getDataTable(list);
    }

    /**
     * 导出档口商品颜色的尺码列表
     */
    @PreAuthorize("@ss.hasPermi('system:size:export')")
    @Log(title = "档口商品颜色的尺码", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductColorSize storeProductColorSize) {
        List<StoreProductColorSize> list = storeProductColorSizeService.selectStoreProductColorSizeList(storeProductColorSize);
        ExcelUtil<StoreProductColorSize> util = new ExcelUtil<StoreProductColorSize>(StoreProductColorSize.class);
        util.exportExcel(response, list, "档口商品颜色的尺码数据");
    }

    /**
     * 获取档口商品颜色的尺码详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:size:query')")
    @GetMapping(value = "/{storeProdColorSizeId}")
    public AjaxResult getInfo(@PathVariable("storeProdColorSizeId") Long storeProdColorSizeId) {
        return success(storeProductColorSizeService.selectStoreProductColorSizeByStoreProdColorSizeId(storeProdColorSizeId));
    }

    /**
     * 新增档口商品颜色的尺码
     */
    @PreAuthorize("@ss.hasPermi('system:size:add')")
    @Log(title = "档口商品颜色的尺码", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoreProductColorSize storeProductColorSize) {
        return toAjax(storeProductColorSizeService.insertStoreProductColorSize(storeProductColorSize));
    }

    /**
     * 修改档口商品颜色的尺码
     */
    @PreAuthorize("@ss.hasPermi('system:size:edit')")
    @Log(title = "档口商品颜色的尺码", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoreProductColorSize storeProductColorSize) {
        return toAjax(storeProductColorSizeService.updateStoreProductColorSize(storeProductColorSize));
    }

    /**
     * 删除档口商品颜色的尺码
     */
    @PreAuthorize("@ss.hasPermi('system:size:remove')")
    @Log(title = "档口商品颜色的尺码", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdColorSizeIds}")
    public AjaxResult remove(@PathVariable Long[] storeProdColorSizeIds) {
        return toAjax(storeProductColorSizeService.deleteStoreProductColorSizeByStoreProdColorSizeIds(storeProdColorSizeIds));
    }
}
