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
import com.ruoyi.bookSys.domain.AccessLogs;
import com.ruoyi.bookSys.service.IAccessLogsService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 通行记录Controller
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
@RestController
@RequestMapping("/bookSys/logs")
public class AccessLogsController extends BaseController
{
    @Autowired
    private IAccessLogsService accessLogsService;

    /**
     * 查询通行记录列表
     */
    @PreAuthorize("@ss.hasPermi('bookSys:logs:list')")
    @GetMapping("/list")
    public TableDataInfo list(AccessLogs accessLogs)
    {
        startPage();
        List<AccessLogs> list = accessLogsService.selectAccessLogsList(accessLogs);
        return getDataTable(list);
    }

    /**
     * 导出通行记录列表
     */
    @PreAuthorize("@ss.hasPermi('bookSys:logs:export')")
    @Log(title = "通行记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AccessLogs accessLogs)
    {
        List<AccessLogs> list = accessLogsService.selectAccessLogsList(accessLogs);
        ExcelUtil<AccessLogs> util = new ExcelUtil<AccessLogs>(AccessLogs.class);
        util.exportExcel(response, list, "通行记录数据");
    }

    /**
     * 获取通行记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('bookSys:logs:query')")
    @GetMapping(value = "/{logId}")
    public AjaxResult getInfo(@PathVariable("logId") Long logId)
    {
        return success(accessLogsService.selectAccessLogsByLogId(logId));
    }

    /**
     * 新增通行记录
     */
    @PreAuthorize("@ss.hasPermi('bookSys:logs:add')")
    @Log(title = "通行记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AccessLogs accessLogs)
    {
        return toAjax(accessLogsService.insertAccessLogs(accessLogs));
    }

    /**
     * 修改通行记录
     */
    @PreAuthorize("@ss.hasPermi('bookSys:logs:edit')")
    @Log(title = "通行记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AccessLogs accessLogs)
    {
        return toAjax(accessLogsService.updateAccessLogs(accessLogs));
    }

    /**
     * 删除通行记录
     */
    @PreAuthorize("@ss.hasPermi('bookSys:logs:remove')")
    @Log(title = "通行记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{logIds}")
    public AjaxResult remove(@PathVariable Long[] logIds)
    {
        return toAjax(accessLogsService.deleteAccessLogsByLogIds(logIds));
    }
}
