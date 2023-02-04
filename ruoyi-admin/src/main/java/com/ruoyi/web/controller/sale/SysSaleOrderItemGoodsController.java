package com.ruoyi.web.controller.sale;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.sale.domain.SysSaleOrderItemGoods;
import com.ruoyi.sale.service.ISysSaleOrderItemGoodsService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 订单商品Controller
 *
 * @author zwgu
 * @date 2022-03-01
 */
@RestController
@RequestMapping("/sale/saleGoods")
public class SysSaleOrderItemGoodsController extends BaseController {
    @Autowired
    private ISysSaleOrderItemGoodsService sysSaleOrderItemGoodsService;

    /**
     * 查询订单商品列表
     */
    @PreAuthorize("@ss.hasPermi('sale:saleGoods:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysSaleOrderItemGoods sysSaleOrderItemGoods) {
        startPage();
        List<SysSaleOrderItemGoods> list = sysSaleOrderItemGoodsService.selectSysSaleOrderItemGoodsList(sysSaleOrderItemGoods);
        return getDataTable(list);
    }

    /**
     * 导出订单商品列表
     */
    @PreAuthorize("@ss.hasPermi('sale:saleGoods:export')")
    @Log(title = "订单商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysSaleOrderItemGoods sysSaleOrderItemGoods) {
        List<SysSaleOrderItemGoods> list = sysSaleOrderItemGoodsService.selectSysSaleOrderItemGoodsList(sysSaleOrderItemGoods);
        ExcelUtil<SysSaleOrderItemGoods> util = new ExcelUtil<SysSaleOrderItemGoods>(SysSaleOrderItemGoods.class);
        util.exportExcel(response, list, "订单商品数据");
    }

    /**
     * 获取订单商品详细信息
     */
    @PreAuthorize("@ss.hasPermi('sale:saleGoods:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysSaleOrderItemGoodsService.selectSysSaleOrderItemGoodsById(id));
    }

    /**
     * 新增订单商品
     */
    @PreAuthorize("@ss.hasPermi('sale:saleGoods:add')")
    @Log(title = "订单商品", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysSaleOrderItemGoods sysSaleOrderItemGoods) {
        return toAjax(sysSaleOrderItemGoodsService.insertSysSaleOrderItemGoods(sysSaleOrderItemGoods));
    }

    /**
     * 修改订单商品
     */
    @PreAuthorize("@ss.hasPermi('sale:saleGoods:edit')")
    @Log(title = "订单商品", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysSaleOrderItemGoods sysSaleOrderItemGoods) {
        return toAjax(sysSaleOrderItemGoodsService.updateSysSaleOrderItemGoods(sysSaleOrderItemGoods));
    }

    /**
     * 删除订单商品
     */
    @PreAuthorize("@ss.hasPermi('sale:saleGoods:remove')")
    @Log(title = "订单商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysSaleOrderItemGoodsService.deleteSysSaleOrderItemGoodsByIds(ids));
    }
}
