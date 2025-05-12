package com.easycode.cloud.controller;

import com.easycode.cloud.domain.SaleReturnOrderDetail;
import com.easycode.cloud.service.ISaleReturnOrderDetailService;
import com.weifu.cloud.common.core.utils.poi.ExcelUtil;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessType;
import com.weifu.cloud.common.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 销售发货退货单明细Controller
 *
 * @author fsc
 * @date 2023-03-11
 */
@RestController
@RequestMapping("/saleReturnDetail")
public class SaleReturnOrderDetailController extends BaseController
{
    @Autowired
    private ISaleReturnOrderDetailService saleReturnOrderDetailService;

    /**
     * 查询销售发货退货单明细列表
     */

    @GetMapping("/list")
    public TableDataInfo list(SaleReturnOrderDetail saleReturnOrderDetail)
    {
        startPage();
        List<SaleReturnOrderDetail> list = saleReturnOrderDetailService.selectSaleReturnOrderDetailList(saleReturnOrderDetail);
        return getDataTable(list);
    }

    /**
     * 导出销售发货退货单明细列表
     */
    @RequiresPermissions("stockin:saleReturnDetail:export")
    @Log(title = "销售发货退货单明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SaleReturnOrderDetail saleReturnOrderDetail)
    {
        List<SaleReturnOrderDetail> list = saleReturnOrderDetailService.selectSaleReturnOrderDetailList(saleReturnOrderDetail);
        ExcelUtil<SaleReturnOrderDetail> util = new ExcelUtil<SaleReturnOrderDetail>(SaleReturnOrderDetail.class);
        util.exportExcel(response, list, "销售发货退货单明细数据");
    }

    /**
     * 获取销售发货退货单明细详细信息
     */
    @RequiresPermissions("stockin:saleReturnDetail:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(saleReturnOrderDetailService.selectSaleReturnOrderDetailById(id));
    }

    /**
     * 获取销售发货退货单明细详细信息
     */
    @GetMapping(value = "/getPrintInfoList/{id}")
    public AjaxResult getPrintInfoList(@PathVariable("id") Long id)
    {
        return success(saleReturnOrderDetailService.getPrintInfoList(id));
    }

    /**
     * 新增销售发货退货单明细
     */
    @RequiresPermissions("stockin:saleReturnDetail:add")
    @Log(title = "销售发货退货单明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SaleReturnOrderDetail saleReturnOrderDetail)
    {
        return toAjax(saleReturnOrderDetailService.insertSaleReturnOrderDetail(saleReturnOrderDetail));
    }

    /**
     * 修改销售发货退货单明细
     */
    @RequiresPermissions("stockin:saleReturnDetail:edit")
    @Log(title = "销售发货退货单明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SaleReturnOrderDetail saleReturnOrderDetail)
    {
        return toAjax(saleReturnOrderDetailService.updateSaleReturnOrderDetail(saleReturnOrderDetail));
    }

    /**
     * 删除销售发货退货单明细
     */
    @RequiresPermissions("stockin:saleReturnDetail:remove")
    @Log(title = "销售发货退货单明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(saleReturnOrderDetailService.deleteSaleReturnOrderDetailByIds(ids));
    }
}
