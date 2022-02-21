package com.ruoyi.web.controller.sale;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.sale.domain.SysSaleOrder;
import com.ruoyi.sale.service.ISysSaleOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 销售订单Controller
 *
 * @author zwgu
 * @date 2022-02-21
 */
@RestController
@RequestMapping("/sale/saleOrder")
public class SysSaleOrderController extends BaseController {
    @Autowired
    private ISysSaleOrderService sysSaleOrderService;

    /**
     * 查询销售订单列表
     */
    @PreAuthorize("@ss.hasPermi('sale:saleOrder:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysSaleOrder sysSaleOrder) {
        startPage();
        List<SysSaleOrder> list = sysSaleOrderService.selectSysSaleOrderList(sysSaleOrder);
        return getDataTable(list);
    }

    /**
     * 导出销售订单列表
     */
    @PreAuthorize("@ss.hasPermi('sale:saleOrder:export')")
    @Log(title = "销售订单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysSaleOrder sysSaleOrder) {
        List<SysSaleOrder> list = sysSaleOrderService.selectSysSaleOrderList(sysSaleOrder);
        ExcelUtil<SysSaleOrder> util = new ExcelUtil<SysSaleOrder>(SysSaleOrder.class);
        util.exportExcel(response, list, "销售订单数据");
    }

    /**
     * 获取销售订单详细信息
     */
    @PreAuthorize("@ss.hasPermi('sale:saleOrder:query')")
    @GetMapping(value = "/{orderId}")
    public AjaxResult getInfo(@PathVariable("orderId") Long orderId) {
        return AjaxResult.success(sysSaleOrderService.selectSysSaleOrderByOrderId(orderId));
    }

    /**
     * 新增销售订单
     */
    @PreAuthorize("@ss.hasPermi('sale:saleOrder:add')")
    @Log(title = "销售订单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysSaleOrder sysSaleOrder) {
        return toAjax(sysSaleOrderService.insertSysSaleOrder(sysSaleOrder));
    }

    /**
     * 修改销售订单
     */
    @PreAuthorize("@ss.hasPermi('sale:saleOrder:edit')")
    @Log(title = "销售订单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysSaleOrder sysSaleOrder) {
        return toAjax(sysSaleOrderService.updateSysSaleOrder(sysSaleOrder));
    }

    /**
     * 删除销售订单
     */
    @PreAuthorize("@ss.hasPermi('sale:saleOrder:remove')")
    @Log(title = "销售订单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{orderIds}")
    public AjaxResult remove(@PathVariable Long[] orderIds) {
        return toAjax(sysSaleOrderService.deleteSysSaleOrderByOrderIds(orderIds));
    }
}
