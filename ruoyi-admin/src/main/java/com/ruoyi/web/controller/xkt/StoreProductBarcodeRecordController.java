package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreProductBarcodeRecord;
import com.ruoyi.xkt.service.IStoreProductBarcodeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口打印条形码记录Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/barcode-records")
public class StoreProductBarcodeRecordController extends XktBaseController {
    @Autowired
    private IStoreProductBarcodeRecordService storeProductBarcodeRecordService;

    // TODO 商品 销售/出库 时扫条码查询信息
    // TODO 商品 销售/出库 时扫条码查询信息
    // TODO 商品 销售/出库 时扫条码查询信息

    /**
     * 查询档口打印条形码记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductBarcodeRecord storeProductBarcodeRecord) {
        startPage();
        List<StoreProductBarcodeRecord> list = storeProductBarcodeRecordService.selectStoreProductBarcodeRecordList(storeProductBarcodeRecord);
        return getDataTable(list);
    }

    /**
     * 导出档口打印条形码记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:record:export')")
    @Log(title = "档口打印条形码记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductBarcodeRecord storeProductBarcodeRecord) {
        List<StoreProductBarcodeRecord> list = storeProductBarcodeRecordService.selectStoreProductBarcodeRecordList(storeProductBarcodeRecord);
        ExcelUtil<StoreProductBarcodeRecord> util = new ExcelUtil<StoreProductBarcodeRecord>(StoreProductBarcodeRecord.class);
        util.exportExcel(response, list, "档口打印条形码记录数据");
    }

    /**
     * 获取档口打印条形码记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:record:query')")
    @GetMapping(value = "/{storeProdBarcodeRecordId}")
    public R getInfo(@PathVariable("storeProdBarcodeRecordId") Long storeProdBarcodeRecordId) {
        return success(storeProductBarcodeRecordService.selectStoreProductBarcodeRecordByStoreProdBarcodeRecordId(storeProdBarcodeRecordId));
    }

    /**
     * 新增档口打印条形码记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:add')")
    @Log(title = "档口打印条形码记录", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreProductBarcodeRecord storeProductBarcodeRecord) {
        return success(storeProductBarcodeRecordService.insertStoreProductBarcodeRecord(storeProductBarcodeRecord));
    }

    /**
     * 修改档口打印条形码记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:edit')")
    @Log(title = "档口打印条形码记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreProductBarcodeRecord storeProductBarcodeRecord) {
        return success(storeProductBarcodeRecordService.updateStoreProductBarcodeRecord(storeProductBarcodeRecord));
    }

    /**
     * 删除档口打印条形码记录
     */
    @PreAuthorize("@ss.hasPermi('system:record:remove')")
    @Log(title = "档口打印条形码记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdBarcodeRecordIds}")
    public R remove(@PathVariable Long[] storeProdBarcodeRecordIds) {
        return success(storeProductBarcodeRecordService.deleteStoreProductBarcodeRecordByStoreProdBarcodeRecordIds(storeProdBarcodeRecordIds));
    }
}
