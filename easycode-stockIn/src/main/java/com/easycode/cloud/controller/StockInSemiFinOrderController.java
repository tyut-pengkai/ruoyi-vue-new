package com.easycode.cloud.controller;

import com.easycode.cloud.domain.dto.StockInSemiFinOrderDto;
import com.easycode.cloud.domain.vo.StockInSemiFinOrderDetailVo;
import com.easycode.cloud.service.IStockInSemiFinOrderService;
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
 * 半成品入库单Controller
 * 
 * @author bcp
 * @date 2023-07-22
 */
@RestController
@RequestMapping("/stockInSemiFinOrder")
public class StockInSemiFinOrderController extends BaseController
{
    @Autowired
    private IStockInSemiFinOrderService stockInSemiFinOrderService;

    /**
     * 查询半成品入库单列表
     */
    @RequiresPermissions("stockin:stockInSemiFinOrder:list")
    @GetMapping("/list")
    public TableDataInfo list(StockInSemiFinOrderDto dto)
    {
        startPage();
        List<StockInSemiFinOrderDto> list = stockInSemiFinOrderService.selectStockInSemiList(dto);
        return getDataTable(list);
    }

    /**
     * 导出半成品入库单列表
     */
    @RequiresPermissions("stockin:stockInSemiFinOrder:export")
    @Log(title = "半成品入库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockInSemiFinOrderDto dto)
    {
        List<StockInSemiFinOrderDto> list = stockInSemiFinOrderService.selectStockInSemiList(dto);
        ExcelUtil<StockInSemiFinOrderDto> util = new ExcelUtil<StockInSemiFinOrderDto>(StockInSemiFinOrderDto.class);
        util.exportExcel(response, list, "半成品入库单数据");
    }

    /**
     * 获取半成品入库单详细信息
     */
    @RequiresPermissions("stockin:stockInSemiFinOrder:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(stockInSemiFinOrderService.selectStockInSemiFinOrderById(id));
    }

    /**
     * 新增半成品入库单
     */
    @RequiresPermissions("stockin:stockInSemiFinOrder:add")
    @Log(title = "半成品入库单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StockInSemiFinOrderDto stockInSemiFinOrderDto)
    {
        return success(stockInSemiFinOrderService.insertStockInSemiFinOrder(stockInSemiFinOrderDto));
    }

    /**
     * 修改半成品入库单
     */
    @RequiresPermissions("stockin:stockInSemiFinOrder:edit")
    @Log(title = "半成品入库单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockInSemiFinOrderDto stockInSemiFinOrderDto)
    {
        return toAjax(stockInSemiFinOrderService.updateStockInSemiFinOrder(stockInSemiFinOrderDto));
    }

    /**
     * 删除半成品入库单
     */
    @RequiresPermissions("stockin:stockInSemiFinOrder:remove")
    @Log(title = "半成品入库单", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(stockInSemiFinOrderService.deleteStockInSemiFinOrderByIds(ids));
    }

    /**
     * 关闭半成品入库单
     */
    @RequiresPermissions("stockin:stockInSemiFinOrder:close")
    @GetMapping("/closeStockInSemiFinOrder/{ids}")
    @Log(title = "半成品入库单", businessType = BusinessType.UPDATE)
    public AjaxResult closeStockInSemiFinOrder(@PathVariable Long[] ids) {
        return toAjax(stockInSemiFinOrderService.closeStockInSemiFinOrder(ids));
    }

    /**
     * 获取半成品标签打印
     */
    @RequiresPermissions("stockin:stockInSemiFinOrder:print")
    @GetMapping("/getStockInSemiFinOrderPrint")
    public AjaxResult getCpPrint(StockInSemiFinOrderDto stockInSemiFinOrderDto) {
        List<StockInSemiFinOrderDetailVo> list = stockInSemiFinOrderService.getStockInSemiFinOrderPrint(stockInSemiFinOrderDto);
        return success(list);
    }
}
