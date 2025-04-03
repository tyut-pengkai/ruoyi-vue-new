package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreProductStorageDemandDeduct;
import com.ruoyi.xkt.service.IStoreProductStorageDemandDeducteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口商品入库抵扣需求Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/storages-demand-deductes")
public class StoreProductStorageDemandDeducteController extends XktBaseController {
    @Autowired
    private IStoreProductStorageDemandDeducteService storeProductStorageDemandDeducteService;

    /**
     * 查询档口商品入库抵扣需求列表
     */
    @PreAuthorize("@ss.hasPermi('system:deducte:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductStorageDemandDeduct storeProductStorageDemandDeducte) {
        startPage();
        List<StoreProductStorageDemandDeduct> list = storeProductStorageDemandDeducteService.selectStoreProductStorageDemandDeducteList(storeProductStorageDemandDeducte);
        return getDataTable(list);
    }

    /**
     * 导出档口商品入库抵扣需求列表
     */
    @PreAuthorize("@ss.hasPermi('system:deducte:export')")
    @Log(title = "档口商品入库抵扣需求", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductStorageDemandDeduct storeProductStorageDemandDeducte) {
        List<StoreProductStorageDemandDeduct> list = storeProductStorageDemandDeducteService.selectStoreProductStorageDemandDeducteList(storeProductStorageDemandDeducte);
        ExcelUtil<StoreProductStorageDemandDeduct> util = new ExcelUtil<StoreProductStorageDemandDeduct>(StoreProductStorageDemandDeduct.class);
        util.exportExcel(response, list, "档口商品入库抵扣需求数据");
    }

    /**
     * 获取档口商品入库抵扣需求详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:deducte:query')")
    @GetMapping(value = "/{storeProdStorDemaDeducteId}")
    public R getInfo(@PathVariable("storeProdStorDemaDeducteId") Long storeProdStorDemaDeducteId) {
        return success(storeProductStorageDemandDeducteService.selectStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteId(storeProdStorDemaDeducteId));
    }

    /**
     * 新增档口商品入库抵扣需求
     */
    @PreAuthorize("@ss.hasPermi('system:deducte:add')")
    @Log(title = "档口商品入库抵扣需求", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreProductStorageDemandDeduct storeProductStorageDemandDeducte) {
        return success(storeProductStorageDemandDeducteService.insertStoreProductStorageDemandDeducte(storeProductStorageDemandDeducte));
    }

    /**
     * 修改档口商品入库抵扣需求
     */
    @PreAuthorize("@ss.hasPermi('system:deducte:edit')")
    @Log(title = "档口商品入库抵扣需求", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreProductStorageDemandDeduct storeProductStorageDemandDeducte) {
        return success(storeProductStorageDemandDeducteService.updateStoreProductStorageDemandDeducte(storeProductStorageDemandDeducte));
    }

    /**
     * 删除档口商品入库抵扣需求
     */
    @PreAuthorize("@ss.hasPermi('system:deducte:remove')")
    @Log(title = "档口商品入库抵扣需求", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdStorDemaDeducteIds}")
    public R remove(@PathVariable Long[] storeProdStorDemaDeducteIds) {
        return success(storeProductStorageDemandDeducteService.deleteStoreProductStorageDemandDeducteByStoreProdStorDemaDeducteIds(storeProdStorDemaDeducteIds));
    }
}
