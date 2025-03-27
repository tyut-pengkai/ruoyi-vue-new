package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreProductStorage;
import com.ruoyi.xkt.service.IStoreProductStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口商品入库Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/prod-storages")
public class StoreProductStorageController extends XktBaseController {
    @Autowired
    private IStoreProductStorageService storeProductStorageService;

    /**
     * 查询档口商品入库列表
     */
    @PreAuthorize("@ss.hasPermi('system:storage:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductStorage storeProductStorage) {
        startPage();
        List<StoreProductStorage> list = storeProductStorageService.selectStoreProductStorageList(storeProductStorage);
        return getDataTable(list);
    }

    /**
     * 导出档口商品入库列表
     */
    @PreAuthorize("@ss.hasPermi('system:storage:export')")
    @Log(title = "档口商品入库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductStorage storeProductStorage) {
        List<StoreProductStorage> list = storeProductStorageService.selectStoreProductStorageList(storeProductStorage);
        ExcelUtil<StoreProductStorage> util = new ExcelUtil<StoreProductStorage>(StoreProductStorage.class);
        util.exportExcel(response, list, "档口商品入库数据");
    }

    /**
     * 获取档口商品入库详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:storage:query')")
    @GetMapping(value = "/{storeProdStorId}")
    public R getInfo(@PathVariable("storeProdStorId") Long storeProdStorId) {
        return success(storeProductStorageService.selectStoreProductStorageByStoreProdStorId(storeProdStorId));
    }

    /**
     * 新增档口商品入库
     */
    @PreAuthorize("@ss.hasPermi('system:storage:add')")
    @Log(title = "档口商品入库", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreProductStorage storeProductStorage) {
        return success(storeProductStorageService.insertStoreProductStorage(storeProductStorage));
    }

    /**
     * 修改档口商品入库
     */
    @PreAuthorize("@ss.hasPermi('system:storage:edit')")
    @Log(title = "档口商品入库", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreProductStorage storeProductStorage) {
        return success(storeProductStorageService.updateStoreProductStorage(storeProductStorage));
    }

    /**
     * 删除档口商品入库
     */
    @PreAuthorize("@ss.hasPermi('system:storage:remove')")
    @Log(title = "档口商品入库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdStorIds}")
    public R remove(@PathVariable Long[] storeProdStorIds) {
        return success(storeProductStorageService.deleteStoreProductStorageByStoreProdStorIds(storeProdStorIds));
    }
}
