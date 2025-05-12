package com.easycode.cloud.controller;

import com.easycode.cloud.domain.CostCenterReturnOrderDetail;
import com.easycode.cloud.service.ICostCenterReturnOrderDetailService;
import com.easycode.common.core.controller.BaseController;
import com.easycode.common.core.page.TableDataInfo;
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
 * 成本中心退货单明细Controller
 *
 * @author fsc
 * @date 2023-03-11
 */
@RestController
@RequestMapping("/costCenterReturnDetail")
public class CostCenterReturnOrderDetailController extends BaseController
{
    @Autowired
    private ICostCenterReturnOrderDetailService costCenterReturnOrderDetailService;

    /**
     * 查询成本中心退货单明细列表
     */

    @GetMapping("/list")
    public TableDataInfo list(CostCenterReturnOrderDetail costCenterReturnOrderDetail)
    {
        startPage();
        List<CostCenterReturnOrderDetail> list = costCenterReturnOrderDetailService.selectCostCenterReturnOrderDetailList(costCenterReturnOrderDetail);
        return getDataTable(list);
    }

    /**
     * 导出成本中心退货单明细列表
     */
    @RequiresPermissions("stockin:costCenterReturnDetail:export")
    @Log(title = "成本中心退货单明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CostCenterReturnOrderDetail costCenterReturnOrderDetail)
    {
        List<CostCenterReturnOrderDetail> list = costCenterReturnOrderDetailService.selectCostCenterReturnOrderDetailList(costCenterReturnOrderDetail);
        ExcelUtil<CostCenterReturnOrderDetail> util = new ExcelUtil<CostCenterReturnOrderDetail>(CostCenterReturnOrderDetail.class);
        util.exportExcel(response, list, "成本中心退货单明细数据");
    }

    /**
     * 获取成本中心退货单明细详细信息
     */
    @RequiresPermissions("stockin:costCenterReturnDetail:query")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(costCenterReturnOrderDetailService.selectCostCenterReturnOrderDetailById(id));
    }

    /**
     * 获取成本中心退货单明细详细信息
     */
    @GetMapping(value = "/getPrintInfoList/{id}")
    public AjaxResult getPrintInfoList(@PathVariable("id") Long id)
    {
        return success(costCenterReturnOrderDetailService.getPrintInfoList(id));
    }

    /**
     * 新增成本中心退货单明细
     */
    @RequiresPermissions("stockin:costCenterReturnDetail:add")
    @Log(title = "成本中心退货单明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CostCenterReturnOrderDetail costCenterReturnOrderDetail)
    {
        return toAjax(costCenterReturnOrderDetailService.insertCostCenterReturnOrderDetail(costCenterReturnOrderDetail));
    }

    /**
     * 修改成本中心退货单明细
     */
    @RequiresPermissions("stockin:costCenterReturnDetail:edit")
    @Log(title = "成本中心退货单明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CostCenterReturnOrderDetail costCenterReturnOrderDetail)
    {
        return toAjax(costCenterReturnOrderDetailService.updateCostCenterReturnOrderDetail(costCenterReturnOrderDetail));
    }

    /**
     * 删除成本中心退货单明细
     */
    @RequiresPermissions("stockin:costCenterReturnDetail:remove")
    @Log(title = "成本中心退货单明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(costCenterReturnOrderDetailService.deleteCostCenterReturnOrderDetailByIds(ids));
    }
}
