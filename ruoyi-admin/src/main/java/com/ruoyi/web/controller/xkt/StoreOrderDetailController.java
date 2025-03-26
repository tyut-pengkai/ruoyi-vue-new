package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreOrderDetail;
import com.ruoyi.xkt.service.IStoreOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口代发订单明细Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/store-order-details")
public class StoreOrderDetailController extends BaseController {
    @Autowired
    private IStoreOrderDetailService storeOrderDetailService;

    /**
     * 查询档口代发订单明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreOrderDetail storeOrderDetail) {
        startPage();
        List<StoreOrderDetail> list = storeOrderDetailService.selectStoreOrderDetailList(storeOrderDetail);
        return getDataTable(list);
    }

    /**
     * 导出档口代发订单明细列表
     */
    @PreAuthorize("@ss.hasPermi('system:detail:export')")
    @Log(title = "档口代发订单明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreOrderDetail storeOrderDetail) {
        List<StoreOrderDetail> list = storeOrderDetailService.selectStoreOrderDetailList(storeOrderDetail);
        ExcelUtil<StoreOrderDetail> util = new ExcelUtil<StoreOrderDetail>(StoreOrderDetail.class);
        util.exportExcel(response, list, "档口代发订单明细数据");
    }

    /**
     * 获取档口代发订单明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:detail:query')")
    @GetMapping(value = "/{storeOrderDetailId}")
    public AjaxResult getInfo(@PathVariable("storeOrderDetailId") Long storeOrderDetailId) {
        return success(storeOrderDetailService.selectStoreOrderDetailByStoreOrderDetailId(storeOrderDetailId));
    }

    /**
     * 新增档口代发订单明细
     */
    @PreAuthorize("@ss.hasPermi('system:detail:add')")
    @Log(title = "档口代发订单明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoreOrderDetail storeOrderDetail) {
        return toAjax(storeOrderDetailService.insertStoreOrderDetail(storeOrderDetail));
    }

    /**
     * 修改档口代发订单明细
     */
    @PreAuthorize("@ss.hasPermi('system:detail:edit')")
    @Log(title = "档口代发订单明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoreOrderDetail storeOrderDetail) {
        return toAjax(storeOrderDetailService.updateStoreOrderDetail(storeOrderDetail));
    }

    /**
     * 删除档口代发订单明细
     */
    @PreAuthorize("@ss.hasPermi('system:detail:remove')")
    @Log(title = "档口代发订单明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeOrderDetailIds}")
    public AjaxResult remove(@PathVariable Long[] storeOrderDetailIds) {
        return toAjax(storeOrderDetailService.deleteStoreOrderDetailByStoreOrderDetailIds(storeOrderDetailIds));
    }
}
