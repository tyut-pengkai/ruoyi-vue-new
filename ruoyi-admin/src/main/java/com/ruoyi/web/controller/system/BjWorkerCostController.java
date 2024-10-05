package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.BjWorkerCost;
import com.ruoyi.system.service.IBjWorkerCostService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 人工成本Controller
 * 
 * @author ssq
 * @date 2024-10-05
 */
@RestController
@RequestMapping("/system/workercost")
public class BjWorkerCostController extends BaseController
{
    @Autowired
    private IBjWorkerCostService bjWorkerCostService;

    /**
     * 查询人工成本列表
     */
    @PreAuthorize("@ss.hasPermi('system:workercost:list')")
    @GetMapping("/list")
    public TableDataInfo list(BjWorkerCost bjWorkerCost)
    {
        startPage();
        List<BjWorkerCost> list = bjWorkerCostService.selectBjWorkerCostList(bjWorkerCost);
        return getDataTable(list);
    }

    /**
     * 导出人工成本列表
     */
    @PreAuthorize("@ss.hasPermi('system:workercost:export')")
    @Log(title = "人工成本", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BjWorkerCost bjWorkerCost)
    {
        List<BjWorkerCost> list = bjWorkerCostService.selectBjWorkerCostList(bjWorkerCost);
        ExcelUtil<BjWorkerCost> util = new ExcelUtil<BjWorkerCost>(BjWorkerCost.class);
        util.exportExcel(response, list, "人工成本数据");
    }

    /**
     * 获取人工成本详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:workercost:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bjWorkerCostService.selectBjWorkerCostById(id));
    }

    /**
     * 新增人工成本
     */
    @PreAuthorize("@ss.hasPermi('system:workercost:add')")
    @Log(title = "人工成本", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BjWorkerCost bjWorkerCost)
    {
        return toAjax(bjWorkerCostService.insertBjWorkerCost(bjWorkerCost));
    }

    /**
     * 修改人工成本
     */
    @PreAuthorize("@ss.hasPermi('system:workercost:edit')")
    @Log(title = "人工成本", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BjWorkerCost bjWorkerCost)
    {
        return toAjax(bjWorkerCostService.updateBjWorkerCost(bjWorkerCost));
    }

    /**
     * 删除人工成本
     */
    @PreAuthorize("@ss.hasPermi('system:workercost:remove')")
    @Log(title = "人工成本", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bjWorkerCostService.deleteBjWorkerCostByIds(ids));
    }
}
