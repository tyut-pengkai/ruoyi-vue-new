package com.easycode.cloud.controller;

import com.alibaba.nacos.shaded.com.google.protobuf.ServiceException;
import com.easycode.cloud.domain.vo.FinProductScanVo;
import com.easycode.cloud.service.IStockInFinOrderService;
import com.weifu.cloud.common.core.utils.poi.ExcelUtil;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessType;
import com.weifu.cloud.common.security.annotation.RequiresPermissions;
import com.weifu.cloud.domain.StockInFinOrder;
import com.weifu.cloud.domain.StockInFinOrderDetail;
import com.weifu.cloud.domain.dto.StockInFinOrderDto;
import com.weifu.cloud.domain.vo.StockInFinOrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 成品入库单Controller
 *
 * @author weifu
 * @date 2022-12-07
 */
@RestController
@RequestMapping("/stockInFinOrder")
public class StockInFinOrderController extends BaseController {

    @Autowired
    private IStockInFinOrderService stockInFinOrderService;

    /**
     * 根据成品收货单号查询打印信息
     */
    @RequiresPermissions("stockin:stockInFinOrder:list")
    @GetMapping("/getPrintInfoByProductNo")
    public AjaxResult getPrintInfoByProductNo(StockInFinOrder stockInFinOrder) {
        List<StockInFinOrderDto> list = stockInFinOrderService.getPrintInfoByProductNo(stockInFinOrder);
        return success(list);
    }

    /**
     * 查询成品入库单列表
     */
    @RequiresPermissions("stockin:stockInFinOrder:list")
    @GetMapping("/list")
    public TableDataInfo list(StockInFinOrder stockInFinOrder) {
        startPage();
        List<StockInFinOrderDto> list = stockInFinOrderService.selectStockInFinOrderList(stockInFinOrder);
        return getDataTable(list);
    }

    /**
     * 导出成品入库单列表
     */
    @RequiresPermissions("stockin:stockInFinOrder:export")
    @Log(title = "成品入库单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StockInFinOrder stockInFinOrder) {
        List<StockInFinOrderDto> list = stockInFinOrderService.selectStockInFinOrderList(stockInFinOrder);
        ExcelUtil<StockInFinOrderDto> util = new ExcelUtil<StockInFinOrderDto>(StockInFinOrderDto.class);
        util.exportExcel(response, list, "成品入库单数据");
    }

    /**
     * 获取成品入库单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(stockInFinOrderService.selectStockInFinOrderById(id));
    }

    /**
     * 新增成品入库单
     */
    @RequiresPermissions("stockin:stockInFinOrder:add")
    @Log(title = "成品入库单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StockInFinOrderDto stockInFinOrderDto) throws ServiceException {
        return success(stockInFinOrderService.insertStockInFinOrder(stockInFinOrderDto));
    }

    /**
     * 编辑成品入库单
     */
    @RequiresPermissions("stockin:stockInFinOrder:editor")
    @Log(title = "成品入库单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockInFinOrderDto stockInFinOrderDto) {
        return toAjax(stockInFinOrderService.updateStockInFinOrder(stockInFinOrderDto));
    }

    /**
     * 删除成品入库单
     */
    @RequiresPermissions("stockin:stockInFinOrder:remove")
    @Log(title = "成品入库单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(stockInFinOrderService.deleteStockInFinOrderByIds(ids));
    }

    /**
     * 查询成品收货明细列表
     */
    @GetMapping("/detail/list")
    public TableDataInfo detailList(StockInFinOrderDetailVo stockInFinOrderDetail) {
        startPage();
        List<StockInFinOrderDetail> list = stockInFinOrderService.selectStockInFinOrderDetailList(stockInFinOrderDetail);
        return getDataTable(list);
    }

    /**
     * 查询成品收货明细列表没有分页
     */
    @GetMapping("/detail/listNoPage")
    public AjaxResult detailListNoPage(StockInFinOrderDetailVo stockInFinOrderDetail) {
        List<StockInFinOrderDetail> list = stockInFinOrderService.selectStockInFinOrderDetailList(stockInFinOrderDetail);
        return AjaxResult.success(list);
    }

    /**
     * 关闭成品入库单
     */
    @RequiresPermissions("stockin:stockInFinOrder:close")
    @GetMapping("/closeStockInFinOrder/{ids}")
    public AjaxResult closeStockInFinOrder(@PathVariable Long[] ids) {
        return toAjax(stockInFinOrderService.closeStockInFinOrder(ids));
    }

    /**
     * 激活成品入库单
     */
    @RequiresPermissions("stockin:stockInFinOrder:active")
    @GetMapping("/activeStockInFinOrderByIds/{ids}")
    public AjaxResult activeStockInFinOrderByIds(@PathVariable Long[] ids) {
        return toAjax(stockInFinOrderService.activeStockInFinOrderByIds(ids));
    }

    /**
     * 成品标签打印
     */
    @RequiresPermissions("stockin:stockInFinOrder:print")
    @GetMapping("/getCpPrint")
    public AjaxResult getCpPrint(StockInFinOrderDetailVo stockInFinOrderDetail) {
        List<StockInFinOrderDetail> list = stockInFinOrderService.selectStockInFinOrderDetailList(stockInFinOrderDetail);
        return success(list);
    }

    /**
     * mom-wms成品收货任务
     */
    @PostMapping("/mesToWmsProductReceiveTask")
    public AjaxResult mesToWmsProductReceiveTask(@RequestParam("json") String json) {
        stockInFinOrderService.mesToWmsProductReceiveTask(json);
        return success();
    }

    /**
     * pda扫描收货生成成品收货单
     *
     * @param finProductScanVo
     * @return
     */
    @PostMapping("/addFinProductTask")
    public AjaxResult addFinProductTask(@RequestBody FinProductScanVo finProductScanVo){
        stockInFinOrderService.addFinProductTask(finProductScanVo);
        return success();
    }

    /**
     * 批量确认
     * @param ids
     * @return
     */
    @GetMapping("/batchConfirm/{ids}")
   public AjaxResult batchConfirm(@PathVariable Long[] ids){
        stockInFinOrderService.batchConfirm(ids);
        return success();
   }
}
