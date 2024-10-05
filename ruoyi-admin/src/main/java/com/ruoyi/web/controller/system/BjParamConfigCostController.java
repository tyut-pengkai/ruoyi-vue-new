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
import com.ruoyi.system.domain.BjParamConfigCost;
import com.ruoyi.system.service.IBjParamConfigCostService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 成本参数Controller
 * 
 * @author ssq
 * @date 2024-10-05
 */
@RestController
@RequestMapping("/system/costparam")
public class BjParamConfigCostController extends BaseController
{
    @Autowired
    private IBjParamConfigCostService bjParamConfigCostService;

    /**
     * 查询成本参数列表
     */
    @PreAuthorize("@ss.hasPermi('system:costparam:list')")
    @GetMapping("/list")
    public TableDataInfo list(BjParamConfigCost bjParamConfigCost)
    {
        startPage();
        List<BjParamConfigCost> list = bjParamConfigCostService.selectBjParamConfigCostList(bjParamConfigCost);
        return getDataTable(list);
    }

    /**
     * 导出成本参数列表
     */
    @PreAuthorize("@ss.hasPermi('system:costparam:export')")
    @Log(title = "成本参数", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BjParamConfigCost bjParamConfigCost)
    {
        List<BjParamConfigCost> list = bjParamConfigCostService.selectBjParamConfigCostList(bjParamConfigCost);
        ExcelUtil<BjParamConfigCost> util = new ExcelUtil<BjParamConfigCost>(BjParamConfigCost.class);
        util.exportExcel(response, list, "成本参数数据");
    }

    /**
     * 获取成本参数详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:costparam:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bjParamConfigCostService.selectBjParamConfigCostById(id));
    }

    /**
     * 新增成本参数
     */
    @PreAuthorize("@ss.hasPermi('system:costparam:add')")
    @Log(title = "成本参数", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BjParamConfigCost bjParamConfigCost)
    {
        return toAjax(bjParamConfigCostService.insertBjParamConfigCost(bjParamConfigCost));
    }

    /**
     * 修改成本参数
     */
    @PreAuthorize("@ss.hasPermi('system:costparam:edit')")
    @Log(title = "成本参数", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BjParamConfigCost bjParamConfigCost)
    {
        return toAjax(bjParamConfigCostService.updateBjParamConfigCost(bjParamConfigCost));
    }

    /**
     * 删除成本参数
     */
    @PreAuthorize("@ss.hasPermi('system:costparam:remove')")
    @Log(title = "成本参数", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bjParamConfigCostService.deleteBjParamConfigCostByIds(ids));
    }
}
