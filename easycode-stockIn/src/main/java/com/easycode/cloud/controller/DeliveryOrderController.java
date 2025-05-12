package com.easycode.cloud.controller;

import com.alibaba.nacos.shaded.com.google.protobuf.ServiceException;
import com.easycode.cloud.service.IDeliveryOrderService;
import com.github.pagehelper.PageHelper;
import com.weifu.cloud.common.core.utils.poi.ExcelUtil;
import com.weifu.cloud.common.core.web.controller.BaseController;
import com.weifu.cloud.common.core.web.domain.AjaxResult;
import com.weifu.cloud.common.core.web.page.TableDataInfo;
import com.weifu.cloud.common.log.annotation.Log;
import com.weifu.cloud.common.log.enums.BusinessType;
import com.weifu.cloud.common.security.annotation.RequiresPermissions;
import com.weifu.cloud.domain.DeliveryOrder;
import com.weifu.cloud.domain.DeliveryOrderDetail;
import com.weifu.cloud.domain.dto.DeliveryOrderDto;
import com.weifu.cloud.domain.vo.DeliveryOrderDetailVo;
import com.weifu.cloud.domain.vo.DeliveryOrderVo;
import com.weifu.cloud.domian.vo.PopupBoxVo;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 送货单Controller
 *
 * @author weifu.cloud
 * @date 2022-11-25
 */
@RestController
@RequestMapping("/deliveryOrder")
public class DeliveryOrderController extends BaseController
{
    @Autowired
    private IDeliveryOrderService deliveryOrderService;

    /**
     * 查询送货单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(DeliveryOrderDto deliveryOrder)
    {
        startPage();
        List<DeliveryOrder> list = deliveryOrderService.selectDeliveryOrderList(deliveryOrder);
        return getDataTable(list);
    }

    /**
     * 导出送货单列表
     */
    @Log(title = "送货单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeliveryOrderDto deliveryOrder)
    {
        List<DeliveryOrder> list = deliveryOrderService.selectDeliveryOrderList(deliveryOrder);
        ExcelUtil<DeliveryOrder> util = new ExcelUtil<>(DeliveryOrder.class);
        util.exportExcel(response, list, "送货单数据");
    }

    /**
     * 获取送货单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(deliveryOrderService.selectDeliveryOrderById(id));
    }

    /**
     * 新增送货单
     */
    @Log(title = "送货单", businessType = BusinessType.INSERT)
    @PostMapping
    @RequiresPermissions("vendor:deliveryOrder:save")
    public AjaxResult add(@RequestBody DeliveryOrderVo deliveryOrderVo) throws Exception {
        return success(deliveryOrderService.insertDeliveryOrder(deliveryOrderVo));
    }

    /**
     * 修改送货单
     */
    @Log(title = "送货单", businessType = BusinessType.UPDATE)
    @PutMapping
    @RequiresPermissions("vendor:deliveryOrder:save")
    public AjaxResult edit(@RequestBody DeliveryOrderVo deliveryOrderVo) throws ServiceException {
        return toAjax(deliveryOrderService.updateDeliveryOrder(deliveryOrderVo));
    }

    /**
     * 新增送货单
     */
    @PostMapping("addNew")
    @RequiresPermissions("vendor:deliveryOrder:save")
    @Log(title = "新增送货单", businessType = BusinessType.INSERT)
    public AjaxResult addNew(@RequestBody DeliveryOrderVo deliveryOrderVo) throws Exception {
        return AjaxResult.success("新增成功",deliveryOrderService.insertDeliveryOrder(deliveryOrderVo));
    }

    /**
     * 删除送货单
     */
    @RequiresPermissions("vendor:deliveryOrder:remove")
    @Log(title = "送货单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(deliveryOrderService.deleteDeliveryOrderByIds(ids));
    }


    /**
     * 发布送货单
     */
    @RequiresPermissions("vendor:deliveryOrder:issue")
    @Log(title = "修改送货单状态", businessType = BusinessType.UPDATE)
    @PostMapping("/updateDeliveryOrderStatus")
    public AjaxResult updateDeliveryOrderStatus(@RequestBody DeliveryOrderVo deliveryOrderVo) throws ServiceException {
        return toAjax(deliveryOrderService.updateDeliveryOrderStatus(deliveryOrderVo));
    }

    /**
     * 保存送货单明细
     */
    @Log(title = "修改收货单详情", businessType = BusinessType.UPDATE)
    @PostMapping("/updateDeliveryOrderDetail")
    public AjaxResult updateDeliveryOrderDetail(@RequestBody DeliveryOrderDetail deliveryOrderDetail){
        return deliveryOrderService.updateDeliveryOrderDetail(deliveryOrderDetail);
    }

    /**
     * 查询送货单明细列表
     */
    @GetMapping("/detail/list")
    public TableDataInfo detailList(DeliveryOrderDetail deliveryOrderDetail)
    {
        startPage();
        List<DeliveryOrderDetailVo> list = deliveryOrderService.selectDeliveryOrderDetailList(deliveryOrderDetail);
        return getDataTable(list);
    }


    /**
     * 查询送货单明细列表没有分页
     */
    @GetMapping("/detail/list/noPage")
    public AjaxResult detailListNoPage(DeliveryOrderDetail deliveryOrderDetail)
    {
        List<DeliveryOrderDetailVo> list = deliveryOrderService.selectDeliveryOrderDetailList(deliveryOrderDetail);
        return AjaxResult.success(list);
    }

    /**
     * 校验送货单明细
     */
    @GetMapping("/detail/{ids}")
    public AjaxResult checkDeliveryOrderDetail(@PathVariable Long[] ids) throws ServiceException {
        return success(deliveryOrderService.checkDeliveryOrderDetail(ids));
    }

    @GetMapping("/queryDeliveryDetail/{id}")
    public AjaxResult queryDeliveryDetail(@PathVariable("id") Long id){
        List<DeliveryOrderDetail> list = deliveryOrderService.queryDeliveryDetailById(id);
        return AjaxResult.success(list);
    }

    /**
     * 送检
     * @param id
     * @return
     * @throws Exception
     */
    @PostMapping("/sendInspectTask/{id}")
    @Log(title = "送检", businessType = BusinessType.UPDATE)
    public AjaxResult sendMessage(@PathVariable("id") Long id) throws Exception {
        return deliveryOrderService.sendMessage(id);
    }



    /**
     * 采购单列表开窗查询
     */
    @PostMapping("/openQuery")
    public TableDataInfo openQuery(@RequestBody PopupBoxVo popupBoxVo)
    {
        PageHelper.startPage(popupBoxVo.getPageNum(), popupBoxVo.getPageSize(), "");
        List<DeliveryOrder> list = deliveryOrderService.openQuery(popupBoxVo);
        return getDataTable(list);
    }
}
