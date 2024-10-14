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
import com.ruoyi.system.domain.TCostQuotationParam;
import com.ruoyi.system.service.ITCostQuotationParamService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 成本报价参数Controller
 * 
 * @author ruoyi
 * @date 2024-10-11
 */
@RestController
@RequestMapping("/system/param")
public class TCostQuotationParamController extends BaseController
{
    @Autowired
    private ITCostQuotationParamService tCostQuotationParamService;

    /**
     * 查询成本报价参数列表
     */
    @PreAuthorize("@ss.hasPermi('system:costparam:list')")
    @GetMapping("/list")
    public TableDataInfo list(TCostQuotationParam tCostQuotationParam)
    {
        startPage();
        List<TCostQuotationParam> list = tCostQuotationParamService.selectTCostQuotationParamList(tCostQuotationParam);
        return getDataTable(list);
    }

    /**
     * 导出成本报价参数列表
     */
    @PreAuthorize("@ss.hasPermi('system:costparam:export')")
    @Log(title = "成本报价参数", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TCostQuotationParam tCostQuotationParam)
    {
        List<TCostQuotationParam> list = tCostQuotationParamService.selectTCostQuotationParamList(tCostQuotationParam);
        ExcelUtil<TCostQuotationParam> util = new ExcelUtil<TCostQuotationParam>(TCostQuotationParam.class);
        util.exportExcel(response, list, "成本报价参数数据");
    }

    /**
     * 获取成本报价参数详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:param:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tCostQuotationParamService.selectTCostQuotationParamById(id));
    }

    /**
     * 新增成本报价参数
     */
    @PreAuthorize("@ss.hasPermi('system:param:add')")
    @Log(title = "成本报价参数", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TCostQuotationParam tCostQuotationParam)
    {
        return toAjax(tCostQuotationParamService.insertTCostQuotationParam(tCostQuotationParam));
    }

    /**
     * 修改成本报价参数
     */
    @PreAuthorize("@ss.hasPermi('system:costparam:edit')")
    @Log(title = "成本报价参数", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TCostQuotationParam tCostQuotationParam)
    {
        return toAjax(tCostQuotationParamService.updateTCostQuotationParam(tCostQuotationParam));
    }

    /**
     * 删除成本报价参数
     */
    @PreAuthorize("@ss.hasPermi('system:costparam:remove')")
    @Log(title = "成本报价参数", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tCostQuotationParamService.deleteTCostQuotationParamByIds(ids));
    }
}
