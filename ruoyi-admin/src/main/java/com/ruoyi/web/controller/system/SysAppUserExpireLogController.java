package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysAppUserExpireLog;
import com.ruoyi.system.service.ISysAppUserExpireLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 时长或点数变动Controller
 *
 * @author zwgu
 * @date 2023-01-05
 */
@RestController
@RequestMapping("/system/appUserExpireLog")
public class SysAppUserExpireLogController extends BaseController {
    @Autowired
    private ISysAppUserExpireLogService sysAppUserExpireLogService;

    /**
     * 查询时长或点数变动列表
     */
    @PreAuthorize("@ss.hasPermi('system:appUserExpireLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysAppUserExpireLog sysAppUserExpireLog) {
        startPage();
        List<SysAppUserExpireLog> list = sysAppUserExpireLogService.selectSysAppUserExpireLogList(sysAppUserExpireLog);
        return getDataTable(list);
    }

    /**
     * 导出时长或点数变动列表
     */
    @PreAuthorize("@ss.hasPermi('system:appUserExpireLog:export')")
    @Log(title = "时长或点数变动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysAppUserExpireLog sysAppUserExpireLog) {
        List<SysAppUserExpireLog> list = sysAppUserExpireLogService.selectSysAppUserExpireLogList(sysAppUserExpireLog);
        ExcelUtil<SysAppUserExpireLog> util = new ExcelUtil<SysAppUserExpireLog>(SysAppUserExpireLog.class);
        util.exportExcel(response, list, "时长或点数变动数据");
    }

    /**
     * 获取时长或点数变动详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:appUserExpireLog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(sysAppUserExpireLogService.selectSysAppUserExpireLogById(id));
    }

    /**
     * 新增时长或点数变动
     */
    @PreAuthorize("@ss.hasPermi('system:appUserExpireLog:add')")
    @Log(title = "时长或点数变动", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAppUserExpireLog sysAppUserExpireLog) {
        return toAjax(sysAppUserExpireLogService.insertSysAppUserExpireLog(sysAppUserExpireLog));
    }

    /**
     * 修改时长或点数变动
     */
    @PreAuthorize("@ss.hasPermi('system:appUserExpireLog:edit')")
    @Log(title = "时长或点数变动", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAppUserExpireLog sysAppUserExpireLog) {
        return toAjax(sysAppUserExpireLogService.updateSysAppUserExpireLog(sysAppUserExpireLog));
    }

    /**
     * 删除时长或点数变动
     */
    @PreAuthorize("@ss.hasPermi('system:appUserExpireLog:remove')")
    @Log(title = "时长或点数变动", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysAppUserExpireLogService.deleteSysAppUserExpireLogByIds(ids));
    }
}
