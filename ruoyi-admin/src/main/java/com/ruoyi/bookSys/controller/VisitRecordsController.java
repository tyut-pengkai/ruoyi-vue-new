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
import com.ruoyi.bookSys.domain.VisitRecords;
import com.ruoyi.bookSys.service.IVisitRecordsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 访客记录Controller
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
@RestController
@RequestMapping("/bookSys/records")
public class VisitRecordsController extends BaseController
{
    @Autowired
    private IVisitRecordsService visitRecordsService;

    /**
     * 查询访客记录列表
     */
    @PreAuthorize("@ss.hasPermi('bookSys:records:list')")
    @GetMapping("/list")
    public TableDataInfo list(VisitRecords visitRecords)
    {
        startPage();
        List<VisitRecords> list = visitRecordsService.selectVisitRecordsList(visitRecords);
        return getDataTable(list);
    }

    /**
     * 导出访客记录列表
     */
    @PreAuthorize("@ss.hasPermi('bookSys:records:export')")
    @Log(title = "访客记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, VisitRecords visitRecords)
    {
        List<VisitRecords> list = visitRecordsService.selectVisitRecordsList(visitRecords);
        ExcelUtil<VisitRecords> util = new ExcelUtil<VisitRecords>(VisitRecords.class);
        util.exportExcel(response, list, "访客记录数据");
    }

    /**
     * 获取访客记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('bookSys:records:query')")
    @GetMapping(value = "/{recordId}")
    public AjaxResult getInfo(@PathVariable("recordId") Long recordId)
    {
        return success(visitRecordsService.selectVisitRecordsByRecordId(recordId));
    }

    /**
     * 新增访客记录
     */
    @PreAuthorize("@ss.hasPermi('bookSys:records:add')")
    @Log(title = "访客记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody VisitRecords visitRecords)
    {
        return toAjax(visitRecordsService.insertVisitRecords(visitRecords));
    }

    /**
     * 修改访客记录
     */
    @PreAuthorize("@ss.hasPermi('bookSys:records:edit')")
    @Log(title = "访客记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody VisitRecords visitRecords)
    {
        return toAjax(visitRecordsService.updateVisitRecords(visitRecords));
    }

    /**
     * 删除访客记录
     */
    @PreAuthorize("@ss.hasPermi('bookSys:records:remove')")
    @Log(title = "访客记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{recordIds}")
    public AjaxResult remove(@PathVariable Long[] recordIds)
    {
        return toAjax(visitRecordsService.deleteVisitRecordsByRecordIds(recordIds));
    }
}
