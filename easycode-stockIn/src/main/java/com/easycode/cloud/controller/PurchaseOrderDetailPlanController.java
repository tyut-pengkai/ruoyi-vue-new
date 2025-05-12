package com.easycode.cloud.controller;

import com.easycode.cloud.domain.dto.PurchaseOrderDetailPlanDto;
import com.easycode.cloud.service.IPurchaseOrderDetailPlanService;
import com.easycode.cloud.service.IPurchaseService;
import com.weifu.cloud.common.core.utils.poi.ExcelUtil;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessType;
import com.weifu.cloud.common.security.annotation.RequiresPermissions;
import com.weifu.cloud.domain.PurchaseOrderDetail;
import com.weifu.cloud.domain.PurchaseOrderDetailPlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 采购协议计划行Controller
 * 
 * @author bcp
 * @date 2023-09-24
 */
@RestController
@RequestMapping("/purchaseOrderDetailPlan")
public class PurchaseOrderDetailPlanController extends BaseController
{
    @Autowired
    private IPurchaseOrderDetailPlanService purchaseOrderDetailPlanService;

    @Autowired
    private IPurchaseService purchaseService;


    /**
     * 查询采购协议计划行列表
     */
    @RequiresPermissions("vendor:purchaseOrderDetailPlan:list")
    @GetMapping("/list")
    public TableDataInfo list(PurchaseOrderDetailPlan purchaseOrderDetailPlan)
    {
        startPage();
        List<PurchaseOrderDetailPlan> list = purchaseOrderDetailPlanService.selectPurchaseOrderDetailPlanList(purchaseOrderDetailPlan);
        return getDataTable(list);
    }

    /**
     * 导出采购协议计划行列表
     */
//    @RequiresPermissions("vendor:purchaseOrderDetailPlan:export")
    @Log(title = "采购协议计划行", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PurchaseOrderDetailPlan purchaseOrderDetailPlan)
    {
        List<PurchaseOrderDetailPlan> list = purchaseOrderDetailPlanService.selectPurchaseOrderDetailPlanList(purchaseOrderDetailPlan);
        ExcelUtil<PurchaseOrderDetailPlan> util = new ExcelUtil<PurchaseOrderDetailPlan>(PurchaseOrderDetailPlan.class);
        util.exportExcel(response, list, "采购协议计划行数据");
    }

    /**
     * 获取采购协议计划行详细信息
     */
//    @RequiresPermissions("vendor:purchaseOrderDetailPlan:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(purchaseOrderDetailPlanService.selectPurchaseOrderDetailPlanById(id));
    }

    /**
     * 新增采购协议计划行
     */
//    @RequiresPermissions("vendor:purchaseOrderDetailPlan:add")
    @Log(title = "采购协议计划行", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PurchaseOrderDetailPlan purchaseOrderDetailPlan)
    {
        return toAjax(purchaseOrderDetailPlanService.insertPurchaseOrderDetailPlan(purchaseOrderDetailPlan));
    }

    /**
     * 修改采购协议计划行
     */
//    @RequiresPermissions("vendor:purchaseOrderDetailPlan:edit")
    @Log(title = "采购协议计划行", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PurchaseOrderDetailPlan purchaseOrderDetailPlan)
    {
        return toAjax(purchaseOrderDetailPlanService.updatePurchaseOrderDetailPlan(purchaseOrderDetailPlan));
    }

    /**
     * 删除采购协议计划行
     */
//    @RequiresPermissions("vendor:purchaseOrderDetailPlan:remove")
    @Log(title = "采购协议计划行", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(purchaseOrderDetailPlanService.deletePurchaseOrderDetailPlanByIds(ids));
    }

    /**
     * 采购单处理页面 查询采购单计划行
     */
    @GetMapping("/queryPurchaseOrderDetailPlan")
    public TableDataInfo queryPurchaseOrderDetailPlan(PurchaseOrderDetailPlanDto purchaseOrderDetailPlanDto)
    {
//        startPage();
        List<PurchaseOrderDetailPlan> list = purchaseOrderDetailPlanService.queryPurchaseOrderDetailPlan(purchaseOrderDetailPlanDto);
        return getDataTable(list);
    }

    /**
     * 采购单处理页面 查询采购单明细行
     */
    @GetMapping("/queryPurchaseOrderDetail")
    public TableDataInfo queryPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail)
    {
        startPage();
        List<PurchaseOrderDetail> list = purchaseService.selectPurchaseOrderDetailList(purchaseOrderDetail);
        return getDataTable(list);
    }
}
