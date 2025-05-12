package com.easycode.cloud.controller;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.easycode.cloud.domain.vo.StockInStdOrderDetailVo;
import com.easycode.cloud.service.IStockInStdOrderService;
import com.easycode.common.annotation.Log;
import com.easycode.common.core.controller.BaseController;
import com.easycode.common.core.domain.AjaxResult;
import com.easycode.common.core.page.TableDataInfo;
import com.easycode.common.enums.BusinessType;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessType;
import com.weifu.cloud.common.security.annotation.RequiresPermissions;
import com.weifu.cloud.domain.StockInItemBom;
import com.weifu.cloud.domain.StockInStdOrder;
import com.weifu.cloud.domain.StockInStdOrderDetail;
import com.weifu.cloud.domain.dto.StockInOrderCommonDto;
import com.weifu.cloud.domain.dto.StockInStdOrderDetailDto;
import com.weifu.cloud.domain.dto.StockInStdOrderDto;
import com.weifu.cloud.domain.vo.PrintInfoVo;
import com.weifu.cloud.util.ResponseResult;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


/**
 * 标准入库单Controller
 *
 * @author weifu
 * @date 2022-12-07
 */
@RestController
@RequestMapping("/stockInStdOrder")
public class StockInStdOrderController extends BaseController
{
    @Autowired
    private IStockInStdOrderService stockInStdOrderService;

    /**
     * 查询标准入库单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(StockInStdOrder stockInStdOrder)
    {
        startPage();
        List<StockInStdOrder> list = stockInStdOrderService.selectStockInStdOrderList(stockInStdOrder);
        return getDataTable(list);
    }

    /**
     * 物料标签打印
     */
//    @RequiresPermissions("stockin:stockInStdOrder:list")
    @GetMapping("/getPrintInfoByIds")
    public AjaxResult getPrintInfoByIds(PrintInfoVo printInfoVo)
    {
        List<PrintInfoVo> list = stockInStdOrderService.getPrintInfoByIds(printInfoVo);
        return success(list);
    }

    /**
     * 获取标准入库单详细信息
     */
    @GetMapping(value = "/{id}")
    public ResponseResult<StockInStdOrder> getInfo(@PathVariable("id") Long id)
    {
        return ResponseResult.okResult(stockInStdOrderService.selectStockInStdOrderById(id));
    }

    /**
     * 关闭标准入库单
     */
    @RequiresPermissions("stockin:stockInStd:close")
    @Log(title = "标准入库单", businessType = BusinessType.UPDATE)
    @PutMapping("add")
    public AjaxResult add(@RequestBody StockInStdOrder stockInStdOrder)
    {
        return AjaxResult.success("添加成功", stockInStdOrderService.addStockInStdOrder(stockInStdOrder));
    }
    /**
     * 关闭标准入库单
     */
    @RequiresPermissions("stockin:stockInStd:close")
    @Log(title = "标准入库单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StockInStdOrder stockInStdOrder)
    {
        return AjaxResult.success("修改成功", stockInStdOrderService.closeStockInStdOrder(stockInStdOrder));
    }

    /**
     * 确认bom
     */
    @Log(title = "确认bom", businessType = BusinessType.UPDATE)
    @PostMapping("readBomSubmit")
    public AjaxResult readBomSubmit(@RequestBody StockInStdOrder stockInStdOrder)
    {
        stockInStdOrderService.readBomSubmit(stockInStdOrder);
        return AjaxResult.success("修改成功");
    }

    /**
     * 标准入库单收货单生成选择列表
     */
    @GetMapping("/stdOrderToDelivery")
    public AjaxResult stdOrderToDelivery(@RequestParam Map<String, Object> map) {
        List<String> orderNos = Convert.toList(String.class, map.get("orderNos"));
        String materialNo = Convert.toStr(map.get("materialNo"));

        return AjaxResult.success(stockInStdOrderService.stdOrderToDeliveryList(materialNo, orderNos));
    }
    /**
     * 查询入标准入口单明细列表
     */
    @GetMapping("/detailList")
    public TableDataInfo listStockInStdOrderDetail(StockInStdOrderDetailDto stockInStdOrderDetailDto)
    {
        startPage();
        List<StockInStdOrderDetail> list = stockInStdOrderService.selectStockInStdOrderDetailList(stockInStdOrderDetailDto);
        return getDataTable(list);
    }

    /**
     * 过账
     */
    @GetMapping("/doPost/{deliveryId}")
    @Log(title = "标准入库过账", businessType = BusinessType.UPDATE)
    @GlobalTransactional
    public AjaxResult doPost(@PathVariable Long deliveryId) throws Exception
    {
        stockInStdOrderService.doPost(deliveryId);
        return AjaxResult.success();
    }

    /**
     * 标准入库任务生成（激活标准入库单）
     */
    @RequiresPermissions("stockin:stockInStd:add")
    @PostMapping("/generateTask")
    public AjaxResult addStockInStdOrderTask(@RequestBody StockInStdOrderDto stockInStdOrderDto) {
        return AjaxResult.success("激活成功", stockInStdOrderService.activeStockInStdOrder(stockInStdOrderDto));
    }

    /**
     * 标准入库生成
     */
    @PostMapping("/syncStockInStdOrder")
    @Log(title = "标准入库同步生成单据", businessType = BusinessType.UPDATE)
    public AjaxResult syncStockInStdOrder(@RequestBody Map<String, Object> params) {
        return AjaxResult.success("查询成功",stockInStdOrderService.syncStockInStdOrder(params));
    }
    /**
     * 成品半成品入库生成
     */
    @PostMapping("/syncStockInSemOrFinOrder")
    @Log(title = "成品半成品入库生成", businessType = BusinessType.UPDATE)
    public AjaxResult syncStockInSemOrFinOrder(@RequestBody Map<String, Object> params) throws Exception {
        List<Map<String, Object>> itemList = (List<Map<String, Object>>) params.get("ITEM");
        return AjaxResult.success("同步成功", stockInStdOrderService.syncStockInSemOrFinOrder(itemList));
    }

    /**
     * 根据送货单id 关闭标准入库单
     */
    @PostMapping("/closeStockInByDeliveryOrderIds")
    public AjaxResult closeStockInByDeliveryOrderIds(@RequestBody Long[] ids) {
        return AjaxResult.success("关闭成功", stockInStdOrderService.closeStockInByDeliveryOrderIds(ids));
    }

    /**
     * 查询入标准入库单明细列表(联查主表)
     */
    @GetMapping("/orderAndDetailList")
    public TableDataInfo queryStdOrderAndDetailList(StockInStdOrderDetail stockInStdOrderDetail)
    {
        startPage();
        List<StockInStdOrderDetailVo> list = stockInStdOrderService.queryStdOrderAndDetailList(stockInStdOrderDetail);
        return getDataTable(list);
    }

    /**
     * 原材料退货新增同步更新标准入库单
     * @param stockInStdOrderDto 标准入库单dto
     * @return 结果
     */
    @Log(title = "标准入库单明细", businessType = BusinessType.UPDATE)
    @PostMapping("/updateStockInStdOrderDetail")
    public AjaxResult editDetail(@RequestBody StockInStdOrderDto stockInStdOrderDto)
    {
        return toAjax(stockInStdOrderService.editDetail(stockInStdOrderDto));
    }

    /**
     * 批量激活标准入库单
     */
    @Log(title = "其它入库单", businessType = BusinessType.UPDATE)
    @GetMapping("/activeStockInStdByIds/{ids}")
    public AjaxResult activeStockInStdByIds(@PathVariable Long[] ids)
    {
        return toAjax(stockInStdOrderService.activeStockInStdByIds(ids));
    }

    /**
     * 根据物料list、单据状态list查询入库单明细数据，将数据以物料代码分组后返回
     * @param stock 库内Dto
     * @Date 2024/5/13
     * @Author fsc
     * @return 根据物料代码分组后的数据集合
     */
    @PostMapping("/queryOnWayNumGroupByMaterialNo")
    public AjaxResult queryOnWayNumGroupByMaterialNo(@RequestBody StockInOrderCommonDto stock)
    {
        return AjaxResult.success("操作成功", JSON.toJSONString(stockInStdOrderService.queryOnWayNumGroupByMaterialNo(stock)));
    }

    @GetMapping("/getWmsStockinStdOrderDetailById/{id}")
    public AjaxResult getWmsStockinStdOrderDetailById(@PathVariable("id") Long id){
        List<StockInStdOrderDetail> stockInStdOrderDetails = stockInStdOrderService.queryWmsStockinStdOrderDetailById(id);
        return AjaxResult.success(stockInStdOrderDetails);
    }

    /**
     * 查询bom信息列表
     * @param id
     * @return
     */
    @GetMapping("/getStockInItemBomById/{id}")
    public TableDataInfo getStockInItemBomById(@PathVariable("id") Long id){
        startPage();
        List<StockInItemBom> list = stockInStdOrderService.getStockInItemBomById(id);
        return getDataTable(list);
    }

    /**
     * 根据任务号查询物料信息
     *
     * @param tastNo 任务号
     * @return 物料信息
     */
    @PostMapping("/getMaterialInfoByTaskNo")
    public AjaxResult getMaterialInfoByTaskNo(@RequestBody String tastNo) throws Exception{
        return success(stockInStdOrderService.getMaterialInfoByTaskNo(tastNo));
    }

}
