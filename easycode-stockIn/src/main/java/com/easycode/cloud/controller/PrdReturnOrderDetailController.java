package com.easycode.cloud.controller;

import com.easycode.cloud.domain.PrdReturnOrderDetail;
import com.easycode.cloud.service.IPrdReturnOrderDetailService;
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
 * 生产订单发货退货单明细Controller
 *
 * @author bcp
 * @date 2023-03-11
 */
@RestController
@RequestMapping("/prdReturnOrderDetail")
public class PrdReturnOrderDetailController extends BaseController
{
    @Autowired
    private IPrdReturnOrderDetailService prdReturnOrderDetailService;

    /**
     * 查询生产订单发货退货单明细列表
     */
    @GetMapping("/list")
    public TableDataInfo list(PrdReturnOrderDetail prdReturnOrderDetail)
    {
        startPage();
        List<PrdReturnOrderDetail> list = prdReturnOrderDetailService.selectPrdReturnOrderDetailList(prdReturnOrderDetail);
        return getDataTable(list);
    }

    /**
     * 退料单物料标签打印
     */
    @GetMapping("/getPrintInfoByIds")
    public AjaxResult getPrintInfoByIds(PrintInfoVo printInfoVo)
    {
        List<PrintInfoVo> list = prdReturnOrderDetailService.getPrintInfoByIds(printInfoVo);
        return success(list);
    }

    /**
     * 根据生产订单退货单号查询
     */
    @GetMapping("/getPrintInfoByProductNo")
    public AjaxResult getPrintInfoByProductNo(PrdReturnOrderDetail prdReturnOrderDetail)
    {
        List<PrdReturnOrderDetail> list = prdReturnOrderDetailService.selectPrdReturnOrderDetailList(prdReturnOrderDetail);
        return success(list);
    }

    /**
     * 导出生产订单发货退货单明细列表
     */
    @RequiresPermissions("stockin:prdReturnOrderDetail:export")
    @Log(title = "生产订单发货退货单明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, PrdReturnOrderDetail prdReturnOrderDetail)
    {
        List<PrdReturnOrderDetail> list = prdReturnOrderDetailService.selectPrdReturnOrderDetailList(prdReturnOrderDetail);
        ExcelUtil<PrdReturnOrderDetail> util = new ExcelUtil<PrdReturnOrderDetail>(PrdReturnOrderDetail.class);
        util.exportExcel(response, list, "生产订单发货退货单明细数据");
    }

    /**
     * 获取生产订单发货退货单明细详细信息
     */
    @RequiresPermissions("stockin:prdReturnOrderDetail:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(prdReturnOrderDetailService.selectPrdReturnOrderDetailById(id));
    }

    /**
     * 新增生产订单发货退货单明细
     */
    @RequiresPermissions("stockin:prdReturnOrderDetail:add")
    @Log(title = "生产订单发货退货单明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody PrdReturnOrderDetail prdReturnOrderDetail)
    {
        return toAjax(prdReturnOrderDetailService.insertPrdReturnOrderDetail(prdReturnOrderDetail));
    }

    /**
     * 修改生产订单发货退货单明细
     */
    @RequiresPermissions("stockin:prdReturnOrderDetail:edit")
    @Log(title = "生产订单发货退货单明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody PrdReturnOrderDetail prdReturnOrderDetail)
    {
        return toAjax(prdReturnOrderDetailService.updatePrdReturnOrderDetail(prdReturnOrderDetail));
    }

    /**
     * 删除生产订单发货退货单明细
     */
    @RequiresPermissions("stockin:prdReturnOrderDetail:remove")
    @Log(title = "生产订单发货退货单明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(prdReturnOrderDetailService.deletePrdReturnOrderDetailByIds(ids));
    }
}
