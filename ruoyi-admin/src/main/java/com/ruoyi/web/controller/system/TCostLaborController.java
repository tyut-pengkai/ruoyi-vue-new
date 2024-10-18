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
import com.ruoyi.system.domain.TCostLabor;
import com.ruoyi.system.service.ITCostLaborService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 人工报价成本Controller
 * 
 * @author ruoyi
 * @date 2024-10-12
 */
@RestController
@RequestMapping("/system/labor")
public class TCostLaborController extends BaseController
{
    @Autowired
    private ITCostLaborService tCostLaborService;

    /**
     * 查询人工报价成本列表
     */
    @PreAuthorize("@ss.hasPermi('system:workercost:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCostLabor tCostLabor)
    {
        startPage();
        List<TCostLabor> list = tCostLaborService.selectTCostLaborList(tCostLabor);
        return getDataTable(list);
    }

    /**
     * 导出人工报价成本列表
     */
    @PreAuthorize("@ss.hasPermi('system:workercost:export')")
    @Log(title = "人工报价成本", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCostLabor tCostLabor)
    {
        List<TCostLabor> list = tCostLaborService.selectTCostLaborList(tCostLabor);
        ExcelUtil<TCostLabor> util = new ExcelUtil<TCostLabor>(TCostLabor.class);
        util.exportExcel(response, list, "人工报价成本数据");
    }

    /**
     * 获取人工报价成本详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:workercost:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tCostLaborService.selectTCostLaborById(id));
    }

    /**
     * 新增人工报价成本
     */
    @PreAuthorize("@ss.hasPermi('system:workercost:add')")
    @Log(title = "人工报价成本", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCostLabor tCostLabor)
    {
        return toAjax(tCostLaborService.insertTCostLabor(tCostLabor));
    }

    /**
     * 修改人工报价成本
     */
    @PreAuthorize("@ss.hasPermi('system:workercost:edit')")
    @Log(title = "人工报价成本", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCostLabor tCostLabor)
    {
        return toAjax(tCostLaborService.updateTCostLabor(tCostLabor));
    }

    /**
     * 删除人工报价成本
     */
    @PreAuthorize("@ss.hasPermi('system:labor:remove')")
    @Log(title = "人工报价成本", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tCostLaborService.deleteTCostLaborByIds(ids));
    }
}
