package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreProductDemandDetail;
import com.ruoyi.xkt.service.IStoreProductDemandDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口商品需求单明细Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/prod-demand-details")
public class StoreProductDemandDetailController extends BaseController {
    @Autowired
    private IStoreProductDemandDetailService storeProductDemandDetailService;

    /**
     * 查询档口商品需求单明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreProductDemandDetail storeProductDemandDetail) {
        startPage();
        List<StoreProductDemandDetail> list = storeProductDemandDetailService.selectStoreProductDemandDetailList(storeProductDemandDetail);
        return getDataTable(list);
    }

    /**
     * 导出档口商品需求单明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:detail:export')")
    @Log(title = "档口商品需求单明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreProductDemandDetail storeProductDemandDetail) {
        List<StoreProductDemandDetail> list = storeProductDemandDetailService.selectStoreProductDemandDetailList(storeProductDemandDetail);
        ExcelUtil<StoreProductDemandDetail> util = new ExcelUtil<StoreProductDemandDetail>(StoreProductDemandDetail.class);
        util.exportExcel(response, list, "档口商品需求单明细数据");
    }

    /**
     * 获取档口商品需求单明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:detail:query')")
    @GetMapping(value = "/{storeProdDemaDetailId}")
    public AjaxResult getInfo(@PathVariable("storeProdDemaDetailId") Long storeProdDemaDetailId) {
        return success(storeProductDemandDetailService.selectStoreProductDemandDetailByStoreProdDemaDetailId(storeProdDemaDetailId));
    }

    /**
     * 新增档口商品需求单明细
     */
    @PreAuthorize("@ss.hasPermi('system:detail:add')")
    @Log(title = "档口商品需求单明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoreProductDemandDetail storeProductDemandDetail) {
        return toAjax(storeProductDemandDetailService.insertStoreProductDemandDetail(storeProductDemandDetail));
    }

    /**
     * 修改档口商品需求单明细
     */
    @PreAuthorize("@ss.hasPermi('system:detail:edit')")
    @Log(title = "档口商品需求单明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoreProductDemandDetail storeProductDemandDetail) {
        return toAjax(storeProductDemandDetailService.updateStoreProductDemandDetail(storeProductDemandDetail));
    }

    /**
     * 删除档口商品需求单明细
     */
    @PreAuthorize("@ss.hasPermi('system:detail:remove')")
    @Log(title = "档口商品需求单明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeProdDemaDetailIds}")
    public AjaxResult remove(@PathVariable Long[] storeProdDemaDetailIds) {
        return toAjax(storeProductDemandDetailService.deleteStoreProductDemandDetailByStoreProdDemaDetailIds(storeProdDemaDetailIds));
    }
}
