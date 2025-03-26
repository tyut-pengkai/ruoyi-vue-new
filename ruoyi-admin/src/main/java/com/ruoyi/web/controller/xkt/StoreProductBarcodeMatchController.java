package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreProductBarcodeMatch;
import com.ruoyi.xkt.service.IStoreProductBarcodeMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口条形码和第三方系统条形码匹配结果Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/barcode-matches")
public class StoreProductBarcodeMatchController extends BaseController {
    @Autowired
    private IStoreProductBarcodeMatchService storeProductBarcodeMatchService;

    /**
     * 查询档口条形码和第三方系统条形码匹配结果列表
     */
    @PreAuthorize("@ss.hasPermi('system:match:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductBarcodeMatch storeProductBarcodeMatch) {
        startPage();
        List<StoreProductBarcodeMatch> list = storeProductBarcodeMatchService.selectStoreProductBarcodeMatchList(storeProductBarcodeMatch);
        return getDataTable(list);
    }

    /**
     * 导出档口条形码和第三方系统条形码匹配结果列表
     */
    @PreAuthorize("@ss.hasPermi('system:match:export')")
    @Log(title = "档口条形码和第三方系统条形码匹配结果", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductBarcodeMatch storeProductBarcodeMatch) {
        List<StoreProductBarcodeMatch> list = storeProductBarcodeMatchService.selectStoreProductBarcodeMatchList(storeProductBarcodeMatch);
        ExcelUtil<StoreProductBarcodeMatch> util = new ExcelUtil<StoreProductBarcodeMatch>(StoreProductBarcodeMatch.class);
        util.exportExcel(response, list, "档口条形码和第三方系统条形码匹配结果数据");
    }

    /**
     * 获取档口条形码和第三方系统条形码匹配结果详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:match:query')")
    @GetMapping(value = "/{storeProdBarcodeMatchId}")
    public AjaxResult getInfo(@PathVariable("storeProdBarcodeMatchId") Long storeProdBarcodeMatchId) {
        return success(storeProductBarcodeMatchService.selectStoreProductBarcodeMatchByStoreProdBarcodeMatchId(storeProdBarcodeMatchId));
    }

    /**
     * 新增档口条形码和第三方系统条形码匹配结果
     */
    @PreAuthorize("@ss.hasPermi('system:match:add')")
    @Log(title = "档口条形码和第三方系统条形码匹配结果", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoreProductBarcodeMatch storeProductBarcodeMatch) {
        return toAjax(storeProductBarcodeMatchService.insertStoreProductBarcodeMatch(storeProductBarcodeMatch));
    }

    /**
     * 修改档口条形码和第三方系统条形码匹配结果
     */
    @PreAuthorize("@ss.hasPermi('system:match:edit')")
    @Log(title = "档口条形码和第三方系统条形码匹配结果", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoreProductBarcodeMatch storeProductBarcodeMatch) {
        return toAjax(storeProductBarcodeMatchService.updateStoreProductBarcodeMatch(storeProductBarcodeMatch));
    }

    /**
     * 删除档口条形码和第三方系统条形码匹配结果
     */
    @PreAuthorize("@ss.hasPermi('system:match:remove')")
    @Log(title = "档口条形码和第三方系统条形码匹配结果", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdBarcodeMatchIds}")
    public AjaxResult remove(@PathVariable Long[] storeProdBarcodeMatchIds) {
        return toAjax(storeProductBarcodeMatchService.deleteStoreProductBarcodeMatchByStoreProdBarcodeMatchIds(storeProdBarcodeMatchIds));
    }
}
