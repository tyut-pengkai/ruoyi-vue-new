package com.easycode.cloud.controller;

import com.easycode.cloud.domain.RwmOutSourceReturnOrder;
import com.easycode.cloud.domain.vo.RwmOutSourceReturnOrderDetailVo;
import com.easycode.cloud.domain.vo.RwmOutSourceReturnOrderVo;
import com.easycode.cloud.service.IRwmOutSourceReturnOrderService;
import com.weifu.cloud.common.core.utils.poi.ExcelUtil;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessType;
import com.weifu.cloud.common.security.annotation.RequiresPermissions;
import com.weifu.cloud.domain.vo.PrintInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 原材料委外发料退退货单Controller
 *
 * @author fsc
 * @date 2023-03-11
 */
@RestController
@RequestMapping("/rwmOutSourceReturn")
public class RwmOutSourceReturnOrderController extends BaseController
{
    @Autowired
    private IRwmOutSourceReturnOrderService rwmOutSourceReturnOrderService;

    /**
     * 查询原材料委外发料退退货单列表
     */
    @RequiresPermissions("stockin:rwmOutSourceOrderReturn:list")
    @GetMapping("/list")
    public TableDataInfo list(RwmOutSourceReturnOrderVo rwmOutSourceReturnOrderVo)
    {
        startPage();
        List<RwmOutSourceReturnOrder> list = rwmOutSourceReturnOrderService.selectRwmOutSourceReturnOrderList(rwmOutSourceReturnOrderVo);
        return getDataTable(list);
    }

    /**
     * 查询原材料委外发料退退货单-物料标签打印
     */
    @GetMapping("/getPrintInfoByIds")
    public AjaxResult getPrintInfoByIds(PrintInfoVo printInfoVo)
    {
        List<PrintInfoVo> list = rwmOutSourceReturnOrderService.getPrintInfoByIds(printInfoVo);
        return success(list);
    }

    /**
     * 导出原材料委外发料退退货单列表
     */
    @RequiresPermissions("stockin:rwmOutSourceReturn:export")
    @Log(title = "原材料委外发料退退货单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RwmOutSourceReturnOrderVo rwmOutSourceReturnOrder)
    {
        List<RwmOutSourceReturnOrder> list = rwmOutSourceReturnOrderService.selectRwmOutSourceReturnOrderList(rwmOutSourceReturnOrder);
        ExcelUtil<RwmOutSourceReturnOrder> util = new ExcelUtil<RwmOutSourceReturnOrder>(RwmOutSourceReturnOrder.class);
        util.exportExcel(response, list, "原材料委外发料退退货单数据");
    }

    /**
     * 获取原材料委外发料退退货单详细信息
     */
    @RequiresPermissions("stockin:rwmOutSourceReturn:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(rwmOutSourceReturnOrderService.selectRwmOutSourceReturnOrderById(id));
    }

    /**
     * 新增原材料委外发料退退货单
     */
    @RequiresPermissions("stockin:rwmOutSourceReturn:add")
    @Log(title = "原材料委外发料退退货单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RwmOutSourceReturnOrder rwmOutSourceReturnOrder)
    {
        return toAjax(rwmOutSourceReturnOrderService.insertRwmOutSourceReturnOrder(rwmOutSourceReturnOrder));
    }

    /**
     * 关闭原材料委外发料退退货单
     */
    @RequiresPermissions("stockin:rwmOutSourceReturn:close")
    @Log(title = "关闭原材料委外发料退退货单", businessType = BusinessType.INSERT)
    @PostMapping(value = "/closeRwmOutSourceOrder")
    public AjaxResult closeRwmOutSourceOrder(@RequestBody RwmOutSourceReturnOrder rwmOutSourceReturnOrder)
    {
        return toAjax(rwmOutSourceReturnOrderService.closeRwmOutSourceOrder(rwmOutSourceReturnOrder));
    }

    /**
     * 新增原材料委外发料退退货单及明细
     */
    @RequiresPermissions("stockin:rwmOutSourceReturn:add")
    @Log(title = "新增原材料委外发料退退货单及明细", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addRwmOutSourceReturn")
    public AjaxResult addRwmOutSourceReturn(@RequestBody RwmOutSourceReturnOrderVo rwmOutSourceReturnOrderVo)
    {
        return success(rwmOutSourceReturnOrderService.addRwmOutSourceReturn(rwmOutSourceReturnOrderVo));
    }

    /**
     * 修改原材料委外发料退退货单
     */
    @RequiresPermissions("stockin:rwmOutSourceReturn:edit")
    @Log(title = "原材料委外发料退退货单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RwmOutSourceReturnOrder rwmOutSourceReturnOrder)
    {
        return toAjax(rwmOutSourceReturnOrderService.updateRwmOutSourceReturnOrder(rwmOutSourceReturnOrder));
    }

    /**
     * 删除原材料委外发料退退货单
     */
    @RequiresPermissions("stockin:rwmOutSourceReturn:remove")
    @Log(title = "原材料委外发料退退货单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(rwmOutSourceReturnOrderService.deleteRwmOutSourceReturnOrderByIds(ids));
    }

    /**
     * 激活原材料委外发料退退货单
     */
    @RequiresPermissions("stockin:rwmOutSourceReturn:active")
    @Log(title = "原材料委外发料退退货单", businessType = BusinessType.UPDATE)
    @GetMapping("/activeRwmOutSourceOrderReturnByIds/{ids}")
    public AjaxResult activeRwmOutSourceOrderReturnByIds(@PathVariable Long[] ids)
    {
        return toAjax(rwmOutSourceReturnOrderService.activeRwmOutSourceOrderReturnByIds(ids));
    }

    @PostMapping({"/importTemplate"})
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<RwmOutSourceReturnOrderDetailVo> util = new ExcelUtil(RwmOutSourceReturnOrderDetailVo.class);
        util.importTemplateExcel(response, "原材料委外发料退数据");
    }

    @RequiresPermissions({"stockin:rwmOutSourceReturn:import"})
    @PostMapping({"/importData"})
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<RwmOutSourceReturnOrderDetailVo> util = new ExcelUtil(RwmOutSourceReturnOrderDetailVo.class);
        List<RwmOutSourceReturnOrderDetailVo> stockList = util.importExcel(file.getInputStream());
        return rwmOutSourceReturnOrderService.importData(stockList);
    }
}
