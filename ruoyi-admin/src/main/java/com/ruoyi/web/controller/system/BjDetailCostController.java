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
import com.ruoyi.system.domain.BjDetailCost;
import com.ruoyi.system.service.IBjDetailCostService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * costController
 * 
 * @author ssq
 * @date 2024-10-04
 */
@RestController
@RequestMapping("/system/cost")
public class BjDetailCostController extends BaseController
{
    @Autowired
    private IBjDetailCostService bjDetailCostService;

    /**
     * 查询cost列表
     */
    @PreAuthorize("@ss.hasPermi('system:cost:list')")
    @GetMapping("/list")
    public TableDataInfo list(BjDetailCost bjDetailCost)
    {
        startPage();
        List<BjDetailCost> list = bjDetailCostService.selectBjDetailCostList(bjDetailCost);
        return getDataTable(list);
    }

    /**
     * 导出cost列表
     */
    @PreAuthorize("@ss.hasPermi('system:cost:export')")
    @Log(title = "cost", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, BjDetailCost bjDetailCost)
    {
        List<BjDetailCost> list = bjDetailCostService.selectBjDetailCostList(bjDetailCost);
        ExcelUtil<BjDetailCost> util = new ExcelUtil<BjDetailCost>(BjDetailCost.class);
        util.exportExcel(response, list, "cost数据");
    }

    /**
     * 获取cost详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:cost:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(bjDetailCostService.selectBjDetailCostById(id));
    }

    /**
     * 新增cost
     */
    @PreAuthorize("@ss.hasPermi('system:cost:add')")
    @Log(title = "cost", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody BjDetailCost bjDetailCost)
    {
        return toAjax(bjDetailCostService.insertBjDetailCost(bjDetailCost));
    }

    /**
     * 修改cost
     */
    @PreAuthorize("@ss.hasPermi('system:cost:edit')")
    @Log(title = "cost", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody BjDetailCost bjDetailCost)
    {
        return toAjax(bjDetailCostService.updateBjDetailCost(bjDetailCost));
    }

    /**
     * 删除cost
     */
    @PreAuthorize("@ss.hasPermi('system:cost:remove')")
    @Log(title = "cost", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(bjDetailCostService.deleteBjDetailCostByIds(ids));
    }
}
