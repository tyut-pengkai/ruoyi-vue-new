package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysAppLogininfor;
import com.ruoyi.system.service.ISysAppLogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统访问记录Controller
 *
 * @author zwgu
 * @date 2021-12-29
 */
@RestController
@RequestMapping("/system/appLogininfor")
public class SysAppLogininforController extends BaseController {
    @Autowired
    private ISysAppLogininforService sysAppLogininforService;

    /**
     * 查询系统访问记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:appLogininfor:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysAppLogininfor sysAppLogininfor) {
        startPage();
        List<SysAppLogininfor> list = sysAppLogininforService.selectSysAppLogininforList(sysAppLogininfor);
        return getDataTable(list);
    }

    /**
     * 导出系统访问记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:appLogininfor:export')")
    @Log(title = "系统访问记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysAppLogininfor sysAppLogininfor) {
        List<SysAppLogininfor> list = sysAppLogininforService.selectSysAppLogininforList(sysAppLogininfor);
        ExcelUtil<SysAppLogininfor> util = new ExcelUtil<SysAppLogininfor>(SysAppLogininfor.class);
        return util.exportExcel(list, "系统访问记录数据");
    }

    /**
     * 获取系统访问记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:appLogininfor:query')")
    @GetMapping(value = "/{infoId}")
    public AjaxResult getInfo(@PathVariable("infoId") Long infoId) {
        return AjaxResult.success(sysAppLogininforService.selectSysAppLogininforByInfoId(infoId));
    }

    /**
     * 新增系统访问记录
     */
    @PreAuthorize("@ss.hasPermi('system:appLogininfor:add')")
    @Log(title = "系统访问记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAppLogininfor sysAppLogininfor) {
        sysAppLogininfor.setCreateBy(getUsername());
        return toAjax(sysAppLogininforService.insertSysAppLogininfor(sysAppLogininfor));
    }

    /**
     * 修改系统访问记录
     */
    @PreAuthorize("@ss.hasPermi('system:appLogininfor:edit')")
    @Log(title = "系统访问记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAppLogininfor sysAppLogininfor) {
        sysAppLogininfor.setUpdateBy(getUsername());
        return toAjax(sysAppLogininforService.updateSysAppLogininfor(sysAppLogininfor));
    }

    /**
     * 删除系统访问记录
     */
    @PreAuthorize("@ss.hasPermi('system:appLogininfor:remove')")
    @Log(title = "系统访问记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        return toAjax(sysAppLogininforService.deleteSysAppLogininforByInfoIds(infoIds));
    }

    @PreAuthorize("@ss.hasPermi('system:appLogininfor:remove')")
    @Log(title = "系统访问记录", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        sysAppLogininforService.cleanLogininfor();
        return AjaxResult.success();
    }
}
