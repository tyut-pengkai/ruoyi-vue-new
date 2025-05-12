package com.easycode.cloud.controller;


import com.easycode.cloud.domain.PurchaseOrder;
import com.easycode.cloud.domain.dto.PurchaseDto;
import com.easycode.cloud.domain.vo.PurchaseVo;
import com.easycode.cloud.service.IPurchaseService;
import com.easycode.cloud.service.IWmsPurchaseOrderRawService;
import com.github.pagehelper.PageHelper;
import com.weifu.cloud.common.core.utils.poi.ExcelUtil;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessType;
import com.weifu.cloud.common.security.annotation.RequiresPermissions;
import com.weifu.cloud.domain.PurchaseOrderDetail;
import com.weifu.cloud.domian.vo.PopupBoxVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 采购单据处理
 * @author hbh
 */
@RestController
@RequestMapping("/purchase")
public class PurchaseController extends BaseController {

    @Autowired
    private IPurchaseService purchaseService;

    @Autowired
    private IWmsPurchaseOrderRawService wmsPurchaseOrderRawService;

    /**
     * 查询采购单据处理列表
     */
    @RequiresPermissions("vendor:purchase:list")
    @GetMapping("/list")
    public TableDataInfo list(PurchaseVo purchaseVo)
    {
        TableDataInfo tableDataInfo = purchaseService.selectWmsPurchaseOrderListShow(purchaseVo);
        return tableDataInfo;
    }

    /**
     * 导出采购单明细列表
     */
    @Log(title = "采购单明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PurchaseVo purchaseVo)
    {
        List<PurchaseDto> list = purchaseService.selectWmsPurchaseOrderList(purchaseVo);
        ExcelUtil<PurchaseDto> util = new ExcelUtil<PurchaseDto>(PurchaseDto.class);
        util.exportExcel(response, list, "采购单明细数据");
    }

    /**
     * 获取采购单明细详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(purchaseService.selectWmsPurchaseOrderDetailById(id));
    }

    /**
     * 新增采购单明细
     */

    @Log(title = "采购单明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody PurchaseOrderDetail wmsPurchaseOrderDetail)
    {
        return toAjax(purchaseService.insertWmsPurchaseOrderDetail(wmsPurchaseOrderDetail));
    }

    /**
     * 修改采购单明细
     */
    @Log(title = "采购单明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody PurchaseOrderDetail wmsPurchaseOrderDetail)
    {
        return toAjax(purchaseService.updateWmsPurchaseOrderDetail(wmsPurchaseOrderDetail));
    }

    /**
     * 删除采购单明细
     */
    @Log(title = "采购单明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(purchaseService.deleteWmsPurchaseOrderDetailByIds(ids));
    }

    /**
     * 手动同步sap采购单据
     */
    @RequiresPermissions("vendor:purchase:purchaseSync")
    @Log(title = "采购单明细", businessType = BusinessType.INSERT)
    @GetMapping("/getOrderFromSap")
    public AjaxResult getOrderFromSap(String orderNo)
    {
        wmsPurchaseOrderRawService.syncPurchaseOrderManual(orderNo);
        return success();
    }

    /**
     * 采购单列表开窗查询
     */
    @PostMapping("/openQuery")
    public TableDataInfo openQuery(@RequestBody PopupBoxVo popupBoxVo)
    {
        PageHelper.startPage(popupBoxVo.getPageNum(), popupBoxVo.getPageSize(), "");
        List<PurchaseOrder> list = purchaseService.openQueryPurchaseOrder(popupBoxVo);
        return getDataTable(list);
    }

    /**
     * 同步交货单
     */
    @RequiresPermissions("vendor:purchase:exchangeOrderSync")
    @GetMapping("/exchangeOrderSync")
    public AjaxResult exchangeOrderSync(String orderNo){
        wmsPurchaseOrderRawService.exchangeOrderSync(orderNo);
        return success();
    }

    /**
     * 同步采购订单(sap-中间表)
     * @return
     */
    @PostMapping(value = "/syncPurchaseOrder")
    public AjaxResult syncPurchaseOrder(@RequestBody String companyCode) {
        wmsPurchaseOrderRawService.syncPurchaseOrder(companyCode);
        return AjaxResult.success();
    }

    /**
     * 同步采购订单信息(中间表-业务表)
     * @return
     */
    @PostMapping(value = "/purOrder/purOrderMidToBusiness")
    public AjaxResult purOrderBusinessSync() {
        wmsPurchaseOrderRawService.purOrderBusinessSync();
        return AjaxResult.success();
    }

    /**
     * 销售发货单同步
     */


    /**
     * 同步采购订单(02067000000006)
     */
    @Log(title = "同步采购订单、采购计划协议同步", businessType = BusinessType.INSERT)
    @PostMapping("syncPurchasePlanOrder")
    public AjaxResult syncPurchasePlanOrder(@RequestBody Map<String, Object> params) {
        List<Map<String, Object>> headList = (List<Map<String, Object>>) params.get("HEAD");
        List<Map<String, Object>> itemList = (List<Map<String, Object>>) params.get("ITEM");

        String status = purchaseService.syncPurchasePlanOrder(headList, itemList);
        return AjaxResult.success(status);
    }

    /**
     * 非ASN入库
     */
    @PostMapping("noAsnStockIn")
    public AjaxResult noAsnStockIn(@RequestBody Map<String, Object> params)
    {
        List<Map<String, Object>> headList = (List<Map<String, Object>>) params.get("HEAD");
        List<Map<String, Object>> itemList = (List<Map<String, Object>>) params.get("ITEM");

        String status = purchaseService.noAsnStockIn(headList, itemList);
        return AjaxResult.success("返回成功",status);
    }
}



