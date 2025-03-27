package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreProductStorageDetail;
import com.ruoyi.xkt.service.IStoreProductStorageDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口商品入库明细Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/storage-details")
public class StoreProductStorageDetailController extends XktBaseController {
    @Autowired
    private IStoreProductStorageDetailService storeProductStorageDetailService;

    /**
     * 查询档口商品入库明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductStorageDetail storeProductStorageDetail) {
        startPage();
        List<StoreProductStorageDetail> list = storeProductStorageDetailService.selectStoreProductStorageDetailList(storeProductStorageDetail);
        return getDataTable(list);
    }

    /**
     * 导出档口商品入库明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:detail:export')")
    @Log(title = "档口商品入库明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductStorageDetail storeProductStorageDetail) {
        List<StoreProductStorageDetail> list = storeProductStorageDetailService.selectStoreProductStorageDetailList(storeProductStorageDetail);
        ExcelUtil<StoreProductStorageDetail> util = new ExcelUtil<StoreProductStorageDetail>(StoreProductStorageDetail.class);
        util.exportExcel(response, list, "档口商品入库明细数据");
    }

    /**
     * 获取档口商品入库明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:detail:query')")
    @GetMapping(value = "/{storeProdStorDetailId}")
    public R getInfo(@PathVariable("storeProdStorDetailId") Long storeProdStorDetailId) {
        return success(storeProductStorageDetailService.selectStoreProductStorageDetailByStoreProdStorDetailId(storeProdStorDetailId));
    }

    /**
     * 新增档口商品入库明细
     */
    @PreAuthorize("@ss.hasPermi('system:detail:add')")
    @Log(title = "档口商品入库明细", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreProductStorageDetail storeProductStorageDetail) {
        return success(storeProductStorageDetailService.insertStoreProductStorageDetail(storeProductStorageDetail));
    }

    /**
     * 修改档口商品入库明细
     */
    @PreAuthorize("@ss.hasPermi('system:detail:edit')")
    @Log(title = "档口商品入库明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreProductStorageDetail storeProductStorageDetail) {
        return success(storeProductStorageDetailService.updateStoreProductStorageDetail(storeProductStorageDetail));
    }

    /**
     * 删除档口商品入库明细
     */
    @PreAuthorize("@ss.hasPermi('system:detail:remove')")
    @Log(title = "档口商品入库明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdStorDetailIds}")
    public R remove(@PathVariable Long[] storeProdStorDetailIds) {
        return success(storeProductStorageDetailService.deleteStoreProductStorageDetailByStoreProdStorDetailIds(storeProdStorDetailIds));
    }
}
