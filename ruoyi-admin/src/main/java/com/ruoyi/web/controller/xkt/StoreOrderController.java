package com.ruoyi.web.controller.xkt;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.XktBaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.xkt.domain.StoreOrder;
import com.ruoyi.xkt.service.IStoreOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 档口代发订单Controller
 *
 * @author ruoyi
 * @date 2025-03-26
 */
@RestController
@RequestMapping("/rest/v1/store-orders")
public class StoreOrderController extends XktBaseController {
    @Autowired
    private IStoreOrderService storeOrderService;

    /**
     * 查询档口代发订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoreOrder storeOrder) {
        startPage();
        List<StoreOrder> list = storeOrderService.selectStoreOrderList(storeOrder);
        return getDataTable(list);
    }

    /**
     * 导出档口代发订单列表
     */
    @PreAuthorize("@ss.hasPermi('system:order:export')")
    @Log(title = "档口代发订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoreOrder storeOrder) {
        List<StoreOrder> list = storeOrderService.selectStoreOrderList(storeOrder);
        ExcelUtil<StoreOrder> util = new ExcelUtil<StoreOrder>(StoreOrder.class);
        util.exportExcel(response, list, "档口代发订单数据");
    }

    /**
     * 获取档口代发订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:order:query')")
    @GetMapping(value = "/{storeOrderId}")
    public R getInfo(@PathVariable("storeOrderId") Long storeOrderId) {
        return success(storeOrderService.selectStoreOrderByStoreOrderId(storeOrderId));
    }

    /**
     * 新增档口代发订单
     */
    @PreAuthorize("@ss.hasPermi('system:order:add')")
    @Log(title = "档口代发订单", businessType = BusinessType.INSERT)
    @PostMapping
    public R add(@RequestBody StoreOrder storeOrder) {
        return success(storeOrderService.insertStoreOrder(storeOrder));
    }

    /**
     * 修改档口代发订单
     */
    @PreAuthorize("@ss.hasPermi('system:order:edit')")
    @Log(title = "档口代发订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public R edit(@RequestBody StoreOrder storeOrder) {
        return success(storeOrderService.updateStoreOrder(storeOrder));
    }

    /**
     * 删除档口代发订单
     */
    @PreAuthorize("@ss.hasPermi('system:order:remove')")
    @Log(title = "档口代发订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{storeOrderIds}")
    public R remove(@PathVariable Long[] storeOrderIds) {
        return success(storeOrderService.deleteStoreOrderByStoreOrderIds(storeOrderIds));
    }
}
