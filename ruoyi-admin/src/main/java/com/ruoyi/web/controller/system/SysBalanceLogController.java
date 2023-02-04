package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.web.service.PermissionService;
import com.ruoyi.system.domain.SysBalanceLog;
import com.ruoyi.system.service.ISysBalanceLogService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 余额变动Controller
 *
 * @author zwgu
 * @date 2022-06-16
 */
@RestController
@RequestMapping("/system/balanceLog")
public class SysBalanceLogController extends BaseController {
    @Autowired
    private ISysBalanceLogService sysBalanceLogService;
    @Resource
    private PermissionService permissionService;

    /**
     * 查询余额变动列表
     */
    @PreAuthorize("@ss.hasPermi('system:balanceLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysBalanceLog sysBalanceLog) {
        startPage();
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            sysBalanceLog.setUserId(getUserId());
        }
        List<SysBalanceLog> list = sysBalanceLogService.selectSysBalanceLogList(sysBalanceLog);
        return getDataTable(list);
    }

    /**
     * 导出余额变动列表
     */
    @PreAuthorize("@ss.hasPermi('system:balanceLog:export')")
    @Log(title = "余额变动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysBalanceLog sysBalanceLog) {
        if (!permissionService.hasAnyRoles("sadmin,admin")) {
            sysBalanceLog.setUserId(getUserId());
        }
        List<SysBalanceLog> list = sysBalanceLogService.selectSysBalanceLogList(sysBalanceLog);
        ExcelUtil<SysBalanceLog> util = new ExcelUtil<SysBalanceLog>(SysBalanceLog.class);
        util.exportExcel(response, list, "余额变动数据");
    }

    /**
     * 获取余额变动详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:balanceLog:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return AjaxResult.success(sysBalanceLogService.selectSysBalanceLogById(id));
    }

    /**
     * 新增余额变动
     */
    @PreAuthorize("@ss.hasPermi('system:balanceLog:add')")
    @Log(title = "余额变动", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysBalanceLog sysBalanceLog) {
        return toAjax(sysBalanceLogService.insertSysBalanceLog(sysBalanceLog));
    }

    /**
     * 修改余额变动
     */
    @PreAuthorize("@ss.hasPermi('system:balanceLog:edit')")
    @Log(title = "余额变动", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysBalanceLog sysBalanceLog) {
        return toAjax(sysBalanceLogService.updateSysBalanceLog(sysBalanceLog));
    }

    /**
     * 删除余额变动
     */
    @PreAuthorize("@ss.hasPermi('system:balanceLog:remove')")
    @Log(title = "余额变动", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(sysBalanceLogService.deleteSysBalanceLogByIds(ids));
    }

    @Log(title = "余额变动", businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermi('system:balanceLog:remove')")
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        sysBalanceLogService.cleanBalanceLog();
        return AjaxResult.success();
    }
}
