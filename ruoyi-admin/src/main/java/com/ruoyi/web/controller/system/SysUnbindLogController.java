package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysUnbindLog;
import com.ruoyi.system.service.ISysUnbindLogService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 解绑日志Controller
 *
 * @author zwgu
 * @date 2023-11-08
 */
@RestController
@RequestMapping("/system/unbindLog")
public class SysUnbindLogController extends BaseController
{
    @Autowired
    private ISysUnbindLogService sysUnbindLogService;

    /**
     * 查询解绑日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:unbindLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysUnbindLog sysUnbindLog)
    {
        startPage();
        List<SysUnbindLog> list = sysUnbindLogService.selectSysUnbindLogList(sysUnbindLog);
        return getDataTable(list);
    }

    /**
     * 导出解绑日志列表
     */
    @PreAuthorize("@ss.hasPermi('system:unbindLog:export')")
    @Log(title = "解绑日志", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysUnbindLog sysUnbindLog)
    {
        List<SysUnbindLog> list = sysUnbindLogService.selectSysUnbindLogList(sysUnbindLog);
        ExcelUtil<SysUnbindLog> util = new ExcelUtil<SysUnbindLog>(SysUnbindLog.class);
        util.exportExcel(response, list, "解绑日志数据");
    }

    /**
     * 获取解绑日志详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:unbindLog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(sysUnbindLogService.selectSysUnbindLogById(id));
    }

    /**
     * 新增解绑日志
     */
    @PreAuthorize("@ss.hasPermi('system:unbindLog:add')")
    @Log(title = "解绑日志", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysUnbindLog sysUnbindLog)
    {
        return toAjax(sysUnbindLogService.insertSysUnbindLog(sysUnbindLog));
    }

    /**
     * 修改解绑日志
     */
    @PreAuthorize("@ss.hasPermi('system:unbindLog:edit')")
    @Log(title = "解绑日志", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysUnbindLog sysUnbindLog)
    {
        return toAjax(sysUnbindLogService.updateSysUnbindLog(sysUnbindLog));
    }

    /**
     * 删除解绑日志
     */
    @PreAuthorize("@ss.hasPermi('system:unbindLog:remove')")
    @Log(title = "解绑日志", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(sysUnbindLogService.deleteSysUnbindLogByIds(ids));
    }
}
