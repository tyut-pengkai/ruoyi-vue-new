package com.easycode.cloud.controller;

import com.easycode.cloud.domain.StockInOther;
import com.easycode.cloud.domain.dto.StockInOtherDto;
import com.easycode.cloud.domain.dto.StockInOtherParamsDto;
import com.easycode.cloud.domain.dto.StockinOtherDetailDto;
import com.easycode.cloud.service.IStockInOtherService;
import com.weifu.cloud.common.core.utils.poi.ExcelUtil;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessType;
import com.weifu.cloud.common.security.annotation.RequiresPermissions;
import com.weifu.cloud.service.IInStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 其它入库单Controller
 * 
 * @author bcp
 * @date 2023-03-24
 */
@RestController
@RequestMapping("/stockInOther")
public class StockInOtherController extends BaseController {
    @Autowired
    private IStockInOtherService stockInOtherService;


    /**
     * 查询其它入库单列表
     */
    @RequiresPermissions("stockin:stockInOther:list")
    @GetMapping("/list")
    public TableDataInfo list(StockInOtherDto stockInOtherDto) {
        startPage();
        List<StockInOther> list = stockInOtherService.selectStockInOtherList(stockInOtherDto);
        return getDataTable(list);
    }

    /**
     * 导出其它入库单列表
     */
    @RequiresPermissions("stockin:stockInOther:export")
    @Log(title = "其它入库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockInOtherDto stockInOtherDto) {
        List<StockInOther> list = stockInOtherService.selectStockInOtherList(stockInOtherDto);
        ExcelUtil<StockInOther> util = new ExcelUtil<StockInOther>(StockInOther.class);
        util.exportExcel(response, list, "其它入库单数据");
    }

    /**
     * 获取其它入库单详细信息
     */
    @RequiresPermissions("stockin:stockInOther:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(stockInOtherService.selectStockInOtherById(id));
    }

    /**
     * 新增其它入库单
     */
    @RequiresPermissions("stockin:stockInOther:add")
    @Log(title = "其它入库单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StockInOtherDto stockInOtherDto) {
        return success(stockInOtherService.insertStockInOther(stockInOtherDto));
    }

    /**
     * 修改其它入库单
     */
    @RequiresPermissions("stockin:stockInOther:edit")
    @Log(title = "其它入库单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockInOther stockInOther) {
        return toAjax(stockInOtherService.updateStockInOther(stockInOther));
    }

    /**
     * 删除其它入库单
     */
    @RequiresPermissions("stockin:stockInOther:remove")
    @Log(title = "其它入库单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(stockInOtherService.deleteStockInOtherByIds(ids));
    }

    /**
     * 关闭其它入库单
     */
    @RequiresPermissions("stockin:stockInOther:close")
    @GetMapping(value = "/close/{id}")
    public AjaxResult close(@PathVariable("id") Long id) {
        return toAjax(stockInOtherService.close(id));
    }

    /**
     * 批量激活其它入库单
     */
    @RequiresPermissions("stockin:stockInOther:active")
    @Log(title = "其它入库单", businessType = BusinessType.UPDATE)
    @GetMapping("/activeStockInOtherByIds/{ids}")
    public AjaxResult activeStockInOtherByIds(@PathVariable Long[] ids) {
        return toAjax(stockInOtherService.activeStockInOtherByIds(ids));
    }

    @GetMapping("/stockInOtherOrderDetailList/{id}")
    public AjaxResult stockInOtherOrderDetailList(@PathVariable Long id) {
        StockinOtherDetailDto list = stockInOtherService.stockInOtherOrderDetailList(id);
        return AjaxResult.success("操作成功", list);
    }

    @GetMapping("/stockInOtherOrderDetailListByTaskNo/{taskNo}")
    public AjaxResult stockInOtherOrderDetailListByTaskNo(@PathVariable String taskNo) {
        StockinOtherDetailDto list = stockInOtherService.stockInOtherOrderDetailListByTaskNo(taskNo);
        return AjaxResult.success("操作成功", list);
    }

    @GetMapping("/stockInOrderList")
    public TableDataInfo list(StockInOtherParamsDto stockInOtherParamsDto) {
        List<StockInOtherParamsDto> list = stockInOtherService.stockInOrderList(stockInOtherParamsDto);
        return getDataTable(list);
    }

    @PutMapping("/submitStockInOrderTask")
    public AjaxResult submitStockInOrderTask(@RequestBody StockinOtherDetailDto stockinOtherDetailDto) throws Exception {
        stockInOtherService.submitStockInOrderTask(stockinOtherDetailDto);
        return AjaxResult.success();
    }
}
