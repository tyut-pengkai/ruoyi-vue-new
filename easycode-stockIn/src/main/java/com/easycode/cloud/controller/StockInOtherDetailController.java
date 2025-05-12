package com.easycode.cloud.controller;

import com.easycode.cloud.domain.StockInOtherDetail;
import com.easycode.cloud.service.IStockInOtherDetailService;
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

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 其它入库明细Controller
 *
 * @author bcp
 * @date 2023-03-24
 */
@RestController
@RequestMapping("/stockInOtherDetail")
public class StockInOtherDetailController extends BaseController
{
    @Autowired
    private IStockInOtherDetailService stockInOtherDetailService;

    /**
     * 查询其它入库明细列表
     */
    @GetMapping("/list")
    public TableDataInfo list(StockInOtherDetail stockInOtherDetail)
    {
        startPage();
        List<StockInOtherDetail> list = stockInOtherDetailService.selectStockInOtherDetailList(stockInOtherDetail);
        return getDataTable(list);
    }

    /**
     * 查询其它入库明细打印信息
     */
    @GetMapping("/getPrintInfoByIds")
    public AjaxResult getPrintInfoByIds(PrintInfoVo printInfoVo)
    {
        List<PrintInfoVo> list = stockInOtherDetailService.getPrintInfoByIds(printInfoVo);
        return success(list);
    }

    /**
     * 导出其它入库明细列表
     */
    @RequiresPermissions("stockin:stockInOtherDetail:export")
    @Log(title = "其它入库明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockInOtherDetail stockInOtherDetail)
    {
        List<StockInOtherDetail> list = stockInOtherDetailService.selectStockInOtherDetailList(stockInOtherDetail);
        ExcelUtil<StockInOtherDetail> util = new ExcelUtil<StockInOtherDetail>(StockInOtherDetail.class);
        util.exportExcel(response, list, "其它入库明细数据");
    }

    /**
     * 获取其它入库明细详细信息
     */
    @RequiresPermissions("stockin:stockInOtherDetail:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(stockInOtherDetailService.selectStockInOtherDetailById(id));
    }

    /**
     * 新增其它入库明细
     */
    @RequiresPermissions("stockin:stockInOtherDetail:add")
    @Log(title = "其它入库明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StockInOtherDetail stockInOtherDetail)
    {
        return toAjax(stockInOtherDetailService.insertStockInOtherDetail(stockInOtherDetail));
    }

    /**
     * 修改其它入库明细
     */
    @RequiresPermissions("stockin:stockInOtherDetail:edit")
    @Log(title = "其它入库明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockInOtherDetail stockInOtherDetail)
    {
        return toAjax(stockInOtherDetailService.updateStockInOtherDetail(stockInOtherDetail));
    }

    /**
     * 删除其它入库明细
     */
    @RequiresPermissions("stockin:stockInOtherDetail:remove")
    @Log(title = "其它入库明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(stockInOtherDetailService.deleteStockInOtherDetailByIds(ids));
    }
}
