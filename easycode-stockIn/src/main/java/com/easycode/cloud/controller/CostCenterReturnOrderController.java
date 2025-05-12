package com.easycode.cloud.controller;

import com.easycode.cloud.domain.CostCenterReturnOrder;
import com.easycode.cloud.domain.dto.RetTaskDto;
import com.easycode.cloud.domain.vo.CostCenterReturnOrderDetailVo;
import com.easycode.cloud.domain.vo.CostCenterReturnOrderVo;
import com.easycode.cloud.domain.vo.RetTaskVo;
import com.easycode.cloud.service.ICostCenterReturnOrderService;
import com.easycode.common.annotation.Log;
import com.easycode.common.core.controller.BaseController;

import com.easycode.common.core.domain.AjaxResult;
import com.easycode.common.core.page.TableDataInfo;
import com.easycode.common.enums.BusinessType;
import com.easycode.common.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 成本中心退货单Controller
 *
 * @author fsc
 * @date 2023-03-11
 */
@RestController
@RequestMapping("/costCenterReturn")
public class CostCenterReturnOrderController extends BaseController
{
    @Autowired
    private ICostCenterReturnOrderService costCenterReturnOrderService;

    /**
     * 查询成本中心退货单列表
     */
    @GetMapping("/list")
    public TableDataInfo list(CostCenterReturnOrderVo costCenterReturnOrderVo)
    {
        startPage();
        List<CostCenterReturnOrder> list = costCenterReturnOrderService.selectCostCenterReturnOrderList(costCenterReturnOrderVo);
        return getDataTable(list);
    }



    /**
     * 导出成本中心退货单列表
     */
    @Log(title = "成本中心退货单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CostCenterReturnOrderVo costCenterReturnOrder)
    {
        List<CostCenterReturnOrder> list = costCenterReturnOrderService.selectCostCenterReturnOrderList(costCenterReturnOrder);
        ExcelUtil<CostCenterReturnOrder> util = new ExcelUtil<CostCenterReturnOrder>(CostCenterReturnOrder.class);
        util.exportExcel(response, list, "成本中心退货单数据");
    }

    /**
     * 获取成本中心退货单详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(costCenterReturnOrderService.selectCostCenterReturnOrderById(id));
    }

    /**
     * 新增成本中心退货单
     */
    @Log(title = "成本中心退货单", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CostCenterReturnOrder costCenterReturnOrder)
    {
        return toAjax(costCenterReturnOrderService.insertCostCenterReturnOrder(costCenterReturnOrder));
    }

    /**
     * 关闭成本中心退货单
     */
    @Log(title = "关闭成本中心退货单", businessType = BusinessType.INSERT)
    @PostMapping(value = "/closeCostCenterOrder")
    public AjaxResult closeCostCenterOrder(@RequestBody CostCenterReturnOrder costCenterReturnOrder)
    {
        return toAjax(costCenterReturnOrderService.closeCostCenterOrder(costCenterReturnOrder));
    }

    /**
     * 新增成本中心退货单及明细
     */
    @Log(title = "新增成本中心退货单及明细", businessType = BusinessType.INSERT)
    @PostMapping(value = "/addCostCenterReturn")
    public AjaxResult addCostCenterReturn(@RequestBody CostCenterReturnOrderVo costCenterReturnOrderVo)
    {
        return success(costCenterReturnOrderService.addCostCenterReturn(costCenterReturnOrderVo));
    }

    /**
     * 修改成本中心退货单
     */
    @Log(title = "成本中心退货单", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CostCenterReturnOrder costCenterReturnOrder)
    {
        return toAjax(costCenterReturnOrderService.updateCostCenterReturnOrder(costCenterReturnOrder));
    }

    /**
     * 删除成本中心退货单
     */
    @Log(title = "成本中心退货单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(costCenterReturnOrderService.deleteCostCenterReturnOrderByIds(ids));
    }

    /**
     * 激活成本中心退货单
     */
    @Log(title = "成本中心退货单", businessType = BusinessType.UPDATE)
    @GetMapping("/activeCostCenterOrderReturnByIds/{ids}")
    public AjaxResult activeCostCenterOrderReturnByIds(@PathVariable Long[] ids)
    {
        return toAjax(costCenterReturnOrderService.activeCostCenterOrderReturnByIds(ids));
    }

    @PostMapping({"/importTemplate"})
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtil<CostCenterReturnOrderDetailVo> util = new ExcelUtil(CostCenterReturnOrderDetailVo.class);
        util.importTemplateExcel(response, "成本中心数据");
    }

    @PostMapping({"/importData"})
    public AjaxResult importData(MultipartFile file) throws Exception {
        ExcelUtil<CostCenterReturnOrderDetailVo> util = new ExcelUtil(CostCenterReturnOrderDetailVo.class);
        List<CostCenterReturnOrderDetailVo> stockList = util.importExcel(file.getInputStream());
        return costCenterReturnOrderService.importData(stockList);
    }

    /**
     * PDA查询成本中心退货单列表
     */
    @GetMapping("/costCenterTaskList")
    public AjaxResult costCenterTaskList(RetTaskDto retTaskDto)
    {
        List<RetTaskVo> list = costCenterReturnOrderService.costCenterTaskList(retTaskDto);
        return AjaxResult.success(list);
    }

    /**
     * PDA获取成本中心退货单详细信息
     */
    @GetMapping(value = "/getCostCenterTaskDetail/{id}")
    public AjaxResult getTaskInfo(@PathVariable("id") Long id)
    {
        return AjaxResult.success(costCenterReturnOrderService.getTaskInfo(id));
    }

    /**
     * PDA完成成本中心退货任务
     * @param retTaskDto
     * @return
     * @throws Exception
     */
    @PostMapping({"/submitCostCenterTask"})
    public AjaxResult submitCostCenterTask(@RequestBody RetTaskDto retTaskDto) throws Exception {
        return AjaxResult.success(costCenterReturnOrderService.submitCostCenterTask(retTaskDto));
    }

}
