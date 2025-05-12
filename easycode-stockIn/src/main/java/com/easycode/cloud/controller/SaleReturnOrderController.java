package com.easycode.cloud.controller;

import com.easycode.cloud.domain.SaleReturnOrder;
import com.easycode.cloud.domain.SaleReturnOrderDetail;
import com.easycode.cloud.domain.vo.SaleReturnOrderDetailVo;
import com.easycode.cloud.domain.vo.SaleReturnOrderVo;
import com.easycode.cloud.service.ISaleReturnOrderService;
import com.weifu.cloud.common.core.utils.poi.ExcelUtil;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessType;
import com.weifu.cloud.common.security.annotation.RequiresPermissions;
import com.weifu.cloud.constant.StockInTaskConstant;
import com.weifu.cloud.domain.vo.PrintInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 销售发货退货单Controller
 *
 * @author fsc
 * @date 2023-03-11
 */
@RestController
@RequestMapping("/saleReturn")
public class SaleReturnOrderController extends BaseController
{
    @Autowired
    private ISaleReturnOrderService saleReturnOrderService;

    @Autowired
    private ISaleReturnOrderService iSaleReturnOrderService;

    /**
     * 查询销售发货退货单列表
     */
    @RequiresPermissions("stockin:saleReturn:list")
    @GetMapping("/list")
    public TableDataInfo list(SaleReturnOrderVo saleReturnOrderVo)
    {
        startPage();
        List<SaleReturnOrder> list = saleReturnOrderService.selectSaleReturnOrderList(saleReturnOrderVo);
        return getDataTable(list);
    }

    /**
     * 查询销售发货退货单-物料标签打印
     */
    @GetMapping("/getPrintInfoByIds")
    public AjaxResult getPrintInfoByIds(PrintInfoVo printInfoVo)
    {
        List<PrintInfoVo> list = saleReturnOrderService.getPrintInfoByIds(printInfoVo);
        return success(list);
    }

    /**
     * 导出销售发货退货单列表
     */
    @RequiresPermissions("stockin:saleReturn:export")
    @Log(title = "销售发货退货单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SaleReturnOrderVo saleReturnOrder)
    {
        List<SaleReturnOrder> list = saleReturnOrderService.selectSaleReturnOrderList(saleReturnOrder);
        ExcelUtil<SaleReturnOrder> util = new ExcelUtil<SaleReturnOrder>(SaleReturnOrder.class);
        util.exportExcel(response, list, "销售发货退货单数据");
    }

    /**
     * 获取销售发货退货单详细信息
     */
    @RequiresPermissions("stockin:saleReturn:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(saleReturnOrderService.selectSaleReturnOrderById(id));
    }

    /**
     * 新增销售发货退货单
     */
    @RequiresPermissions("stockin:saleReturn:add")
    @Log(title = "销售发货退货单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SaleReturnOrder saleReturnOrder)
    {
        return toAjax(saleReturnOrderService.insertSaleReturnOrder(saleReturnOrder));
    }

    /**
     * 关闭销售发货退货单
     */
    @RequiresPermissions("stockin:saleReturn:close")
    @Log(title = "关闭销售发货退货单", businessType = BusinessType.INSERT)
    @PostMapping(value = "/closeSaleOrder")
    public AjaxResult closeSaleOrder(@RequestBody SaleReturnOrder saleReturnOrder)
    {
        return toAjax(saleReturnOrderService.closeSaleOrder(saleReturnOrder));
    }

    /**
     * 新增销售发货退货单及明细
     */
    @RequiresPermissions("stockin:saleReturn:add")
    @Log(title = "新增销售发货退货单及明细", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addSaleReturn")
    public AjaxResult addSaleReturn(@RequestBody SaleReturnOrderVo saleReturnOrderVo)
    {
        return success(saleReturnOrderService.addSaleReturn(saleReturnOrderVo));
    }

    /**
     * 销售外向交货单自动同步，类型销售退的数据
     */
    @PostMapping("sapSalesReturn")
    @Log(title = "销售外向交货单自动同步，类型销售退", businessType = BusinessType.INSERT)
    public AjaxResult sapSalesReturn(@RequestBody Map<String, Object> params)
    {
        List<Map<String, Object>> headList = (List<Map<String, Object>>) params.get("HEAD");
        List<Map<String, Object>> itemList = (List<Map<String, Object>>) params.get("ITEM");

        String status = saleReturnOrderService.sapSalesReturn(headList, itemList);
        return AjaxResult.success("返回成功",status);
    }

    /**
     * 新增销售退任务
     */
    @Log(title = "新增销售退任务", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addRetTask")
    public AjaxResult addRetTask(@RequestBody SaleReturnOrderDetail detail)
    {
        return success(iSaleReturnOrderService.addRetTask(detail, StockInTaskConstant.COST_CENTER_RETURN_TYPE));
    }

    /**
     * 修改销售发货退货单
     */
    @RequiresPermissions("stockin:saleReturn:edit")
    @Log(title = "销售发货退货单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SaleReturnOrder saleReturnOrder)
    {
        return toAjax(saleReturnOrderService.updateSaleReturnOrder(saleReturnOrder));
    }

    /**
     * 删除销售发货退货单
     */
    @RequiresPermissions("stockin:saleReturn:remove")
    @Log(title = "销售发货退货单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(saleReturnOrderService.deleteSaleReturnOrderByIds(ids));
    }

    /**
     * 激活销售发货退货单
     */
    @RequiresPermissions("stockin:saleReturn:active")
    @Log(title = "销售发货退货单", businessType = BusinessType.UPDATE)
    @GetMapping("/activeSaleOrderReturnByIds/{ids}")
    public AjaxResult activeSaleOrderReturnByIds(@PathVariable Long[] ids)
    {
        return toAjax(saleReturnOrderService.activeSaleOrderReturnByIds(ids));
    }

    @PostMapping({"/importTemplate"})
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<SaleReturnOrderDetailVo> util = new ExcelUtil(SaleReturnOrderDetailVo.class);
        util.importTemplateExcel(response, "销售发货数据");
    }

    @RequiresPermissions({"stockin:saleReturn:import"})
    @PostMapping({"/importData"})
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<SaleReturnOrderDetailVo> util = new ExcelUtil(SaleReturnOrderDetailVo.class);
        List<SaleReturnOrderDetailVo> stockList = util.importExcel(file.getInputStream());
        return saleReturnOrderService.importData(stockList);
    }
}
