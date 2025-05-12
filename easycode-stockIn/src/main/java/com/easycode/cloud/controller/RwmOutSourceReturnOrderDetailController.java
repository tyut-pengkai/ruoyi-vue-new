package com.easycode.cloud.controller;

import com.easycode.cloud.domain.RwmOutSourceReturnOrderDetail;
import com.easycode.cloud.service.IRwmOutSourceReturnOrderDetailService;
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
 * 原材料委外发料退退货单明细Controller
 *
 * @author fsc
 * @date 2023-03-11
 */
@RestController
@RequestMapping("/rwmOutSourceReturnDetail")
public class RwmOutSourceReturnOrderDetailController extends BaseController
{
    @Autowired
    private IRwmOutSourceReturnOrderDetailService rwmOutSourceReturnOrderDetailService;

    /**
     * 查询原材料委外发料退退货单明细列表
     */

    @GetMapping("/list")
    public TableDataInfo list(RwmOutSourceReturnOrderDetail rwmOutSourceReturnOrderDetail)
    {
        startPage();
        List<RwmOutSourceReturnOrderDetail> list = rwmOutSourceReturnOrderDetailService.selectRwmOutSourceReturnOrderDetailList(rwmOutSourceReturnOrderDetail);
        return getDataTable(list);
    }

    /**
     * 导出原材料委外发料退退货单明细列表
     */
    @RequiresPermissions("stockin:rwmOutSourceReturnDetail:export")
    @Log(title = "原材料委外发料退退货单明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, RwmOutSourceReturnOrderDetail rwmOutSourceReturnOrderDetail)
    {
        List<RwmOutSourceReturnOrderDetail> list = rwmOutSourceReturnOrderDetailService.selectRwmOutSourceReturnOrderDetailList(rwmOutSourceReturnOrderDetail);
        ExcelUtil<RwmOutSourceReturnOrderDetail> util = new ExcelUtil<RwmOutSourceReturnOrderDetail>(RwmOutSourceReturnOrderDetail.class);
        util.exportExcel(response, list, "原材料委外发料退退货单明细数据");
    }

    /**
     * 获取原材料委外发料退退货单明细详细信息
     */
    @RequiresPermissions("stockin:rwmOutSourceReturnDetail:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(rwmOutSourceReturnOrderDetailService.selectRwmOutSourceReturnOrderDetailById(id));
    }

    /**
     * 获取原材料委外发料退退货单明细详细信息
     */
    @GetMapping(value = "/getPrintInfoList/{id}")
    public AjaxResult getPrintInfoList(@PathVariable("id") Long id)
    {
        return success(rwmOutSourceReturnOrderDetailService.getPrintInfoList(id));
    }

    /**
     * 新增原材料委外发料退退货单明细
     */
    @RequiresPermissions("stockin:rwmOutSourceReturnDetail:add")
    @Log(title = "原材料委外发料退退货单明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody RwmOutSourceReturnOrderDetail rwmOutSourceReturnOrderDetail)
    {
        return toAjax(rwmOutSourceReturnOrderDetailService.insertRwmOutSourceReturnOrderDetail(rwmOutSourceReturnOrderDetail));
    }

    /**
     * 修改原材料委外发料退退货单明细
     */
    @RequiresPermissions("stockin:rwmOutSourceReturnDetail:edit")
    @Log(title = "原材料委外发料退退货单明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody RwmOutSourceReturnOrderDetail rwmOutSourceReturnOrderDetail)
    {
        return toAjax(rwmOutSourceReturnOrderDetailService.updateRwmOutSourceReturnOrderDetail(rwmOutSourceReturnOrderDetail));
    }

    /**
     * 删除原材料委外发料退退货单明细
     */
    @RequiresPermissions("stockin:rwmOutSourceReturnDetail:remove")
    @Log(title = "原材料委外发料退退货单明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(rwmOutSourceReturnOrderDetailService.deleteRwmOutSourceReturnOrderDetailByIds(ids));
    }
}
