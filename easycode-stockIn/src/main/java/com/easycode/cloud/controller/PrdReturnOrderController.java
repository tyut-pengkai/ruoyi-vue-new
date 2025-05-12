package com.easycode.cloud.controller;

import com.easycode.cloud.domain.PrdReturnOrder;
import com.easycode.cloud.domain.dto.PrdReturnOrderDto;
import com.easycode.cloud.service.IPrdReturnOrderService;
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
 * 生产订单发货退料单Controller
 *
 * @author bcp
 * @date 2023-03-11
 */
@RestController
@RequestMapping("/prdReturnOrder")
public class PrdReturnOrderController extends BaseController
{
    @Autowired
    private IPrdReturnOrderService prdReturnOrderService;

    /**
     * 查询生产订单发货退料单列表
     */
    @RequiresPermissions("stockin:prdReturnOrder:list")
    @GetMapping("/list")
    public TableDataInfo list(PrdReturnOrderDto prdReturnOrderDto)
    {
        startPage();
        List<PrdReturnOrder> list = prdReturnOrderService.selectPrdReturnOrderList(prdReturnOrderDto);
        return getDataTable(list);
    }

    /**
     * 导出生产订单发货退料单列表
     */
    @RequiresPermissions("stockin:prdReturnOrder:export")
    @Log(title = "生产订单发货退料单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PrdReturnOrderDto prdReturnOrderDto)
    {
        List<PrdReturnOrder> list = prdReturnOrderService.selectPrdReturnOrderList(prdReturnOrderDto);
        ExcelUtil<PrdReturnOrder> util = new ExcelUtil<PrdReturnOrder>(PrdReturnOrder.class);
        util.exportExcel(response, list, "生产订单发货退料单数据");
    }

    /**
     * 获取生产订单发货退料单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(prdReturnOrderService.selectPrdReturnOrderById(id));
    }

    /**
     * 新增生产订单发货退料单
     */
    @RequiresPermissions("stockin:prdReturnOrder:add")
    @Log(title = "生产订单发货退料单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PrdReturnOrderDto prdReturnOrderDto)
    {
        return success(prdReturnOrderService.insertPrdReturnOrder(prdReturnOrderDto));
    }

    /**
     * 修改生产订单发货退料单
     */
    @RequiresPermissions("stockin:prdReturnOrder:edit")
    @Log(title = "生产订单发货退料单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PrdReturnOrder prdReturnOrder)
    {
        return toAjax(prdReturnOrderService.updatePrdReturnOrder(prdReturnOrder));
    }

    /**
     * 删除生产订单发货退料单
     */
    @RequiresPermissions("stockin:prdReturnOrder:remove")
    @Log(title = "生产订单发货退料单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(prdReturnOrderService.deletePrdReturnOrderByIds(ids));
    }

    /**
     * 关闭生产订单发货退料单
     */
    @RequiresPermissions("stockin:prdReturnOrder:close")
    @GetMapping(value = "/close/{id}")
    public AjaxResult close(@PathVariable("id") Long id)
    {
        return toAjax(prdReturnOrderService.close(id));
    }

    /**
     * 批量激活生产发料退货单
     */
    @RequiresPermissions("stockin:prdReturnOrder:active")
    @Log(title = "生产发料退货单", businessType = BusinessType.UPDATE)
    @GetMapping("/activePrdReturnOrderByIds/{ids}")
    public AjaxResult activePrdReplReturnOrderByIds(@PathVariable Long[] ids)
    {
        return toAjax(prdReturnOrderService.activePrdReturnOrderByIds(ids));
    }
}
