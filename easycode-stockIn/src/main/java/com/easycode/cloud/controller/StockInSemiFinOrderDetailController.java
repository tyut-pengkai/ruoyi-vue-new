package com.easycode.cloud.controller;

import com.easycode.cloud.domain.StockInSemiFinOrderDetail;
import com.easycode.cloud.domain.vo.StockInSemiFinOrderDetailVo;
import com.easycode.cloud.service.IStockInSemiFinOrderDetailService;
import com.weifu.cloud.common.core.utils.poi.ExcelUtil;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 半成品入库单明细Controller
 *
 * @author bcp
 * @date 2023-07-22
 */
@RestController
@RequestMapping("/stockInSemiFinOrderDetail")
public class StockInSemiFinOrderDetailController extends BaseController
{
    @Autowired
    private IStockInSemiFinOrderDetailService stockInSemiFinOrderDetailService;

    /**
     * 查询半成品入库单明细列表
     */
    @GetMapping("/list")
    public TableDataInfo list(StockInSemiFinOrderDetailVo stockInSemiFinOrderDetail)
    {
        startPage();
        List<StockInSemiFinOrderDetailVo> list = stockInSemiFinOrderDetailService.selectStockInSemiFinOrderDetailList(stockInSemiFinOrderDetail);
        return getDataTable(list);
    }

    /**
     * 导出半成品入库单明细列表
     */
    @Log(title = "半成品入库单明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockInSemiFinOrderDetailVo stockInSemiFinOrderDetail)
    {
        List<StockInSemiFinOrderDetailVo> list = stockInSemiFinOrderDetailService.selectStockInSemiFinOrderDetailList(stockInSemiFinOrderDetail);
        ExcelUtil<StockInSemiFinOrderDetailVo> util = new ExcelUtil<>(StockInSemiFinOrderDetailVo.class);
        util.exportExcel(response, list, "半成品入库单明细数据");
    }

    /**
     * 获取半成品入库单明细详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") String id)
    {
        return success(stockInSemiFinOrderDetailService.selectStockInSemiFinOrderDetailById(id));
    }

    /**
     * 新增半成品入库单明细
     */
    @Log(title = "半成品入库单明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StockInSemiFinOrderDetail stockInSemiFinOrderDetail)
    {
        return toAjax(stockInSemiFinOrderDetailService.insertStockInSemiFinOrderDetail(stockInSemiFinOrderDetail));
    }

    /**
     * 修改半成品入库单明细
     */
    @Log(title = "半成品入库单明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockInSemiFinOrderDetail stockInSemiFinOrderDetail)
    {
        return toAjax(stockInSemiFinOrderDetailService.updateStockInSemiFinOrderDetail(stockInSemiFinOrderDetail));
    }

    /**
     * 删除半成品入库单明细
     */
    @Log(title = "半成品入库单明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(stockInSemiFinOrderDetailService.deleteStockInSemiFinOrderDetailByIds(ids));
    }

    /**
     * 查询半成品入库单明细列表(不分页)
     */
    @GetMapping("/listNoPage")
    public AjaxResult listNoPage(StockInSemiFinOrderDetailVo stockInSemiFinOrderDetail)
    {
        List<StockInSemiFinOrderDetailVo> list = stockInSemiFinOrderDetailService.selectStockInSemiFinOrderDetailList(stockInSemiFinOrderDetail);
        return AjaxResult.success(list);
    }
}
