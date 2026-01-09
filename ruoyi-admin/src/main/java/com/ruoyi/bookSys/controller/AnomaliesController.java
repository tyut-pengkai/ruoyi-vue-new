package com.ruoyi.bookSys.controller;

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
import com.ruoyi.bookSys.domain.Anomalies;
import com.ruoyi.bookSys.service.IAnomaliesService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 异常记录Controller
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
@RestController
@RequestMapping("/bookSys/anomalies")
public class AnomaliesController extends BaseController
{
    @Autowired
    private IAnomaliesService anomaliesService;

    /**
     * 查询异常记录列表
     */
    @PreAuthorize("@ss.hasPermi('bookSys:anomalies:list')")
    @GetMapping("/list")
    public TableDataInfo list(Anomalies anomalies)
    {
        startPage();
        List<Anomalies> list = anomaliesService.selectAnomaliesList(anomalies);
        return getDataTable(list);
    }

    /**
     * 导出异常记录列表
     */
    @PreAuthorize("@ss.hasPermi('bookSys:anomalies:export')")
    @Log(title = "异常记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Anomalies anomalies)
    {
        List<Anomalies> list = anomaliesService.selectAnomaliesList(anomalies);
        ExcelUtil<Anomalies> util = new ExcelUtil<Anomalies>(Anomalies.class);
        util.exportExcel(response, list, "异常记录数据");
    }

    /**
     * 获取异常记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('bookSys:anomalies:query')")
    @GetMapping(value = "/{anomalyId}")
    public AjaxResult getInfo(@PathVariable("anomalyId") Long anomalyId)
    {
        return success(anomaliesService.selectAnomaliesByAnomalyId(anomalyId));
    }

    /**
     * 新增异常记录
     */
    @PreAuthorize("@ss.hasPermi('bookSys:anomalies:add')")
    @Log(title = "异常记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Anomalies anomalies)
    {
        return toAjax(anomaliesService.insertAnomalies(anomalies));
    }

    /**
     * 修改异常记录
     */
    @PreAuthorize("@ss.hasPermi('bookSys:anomalies:edit')")
    @Log(title = "异常记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Anomalies anomalies)
    {
        return toAjax(anomaliesService.updateAnomalies(anomalies));
    }

    /**
     * 删除异常记录
     */
    @PreAuthorize("@ss.hasPermi('bookSys:anomalies:remove')")
    @Log(title = "异常记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{anomalyIds}")
    public AjaxResult remove(@PathVariable Long[] anomalyIds)
    {
        return toAjax(anomaliesService.deleteAnomaliesByAnomalyIds(anomalyIds));
    }
}
