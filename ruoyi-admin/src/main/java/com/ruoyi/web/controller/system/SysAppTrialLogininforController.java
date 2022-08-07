package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysAppTrialLogininfor;
import com.ruoyi.system.service.ISysAppTrialLogininforService;
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
@RequestMapping("/system/appTrialLogininfor")
public class SysAppTrialLogininforController extends BaseController {
    @Autowired
    private ISysAppTrialLogininforService sysAppTrialLogininforService;

    /**
     * 查询系统访问记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:appTrialLogininfor:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysAppTrialLogininfor sysAppTrialLogininfor) {
        startPage();
        List<SysAppTrialLogininfor> list = sysAppTrialLogininforService.selectSysAppTrialLogininforList(sysAppTrialLogininfor);
        return getDataTable(list);
    }

    /**
     * 导出系统访问记录列表
     */
    @PreAuthorize("@ss.hasPermi('system:appTrialLogininfor:export')")
    @Log(title = "系统访问记录", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysAppTrialLogininfor sysAppTrialLogininfor) {
        List<SysAppTrialLogininfor> list = sysAppTrialLogininforService.selectSysAppTrialLogininforList(sysAppTrialLogininfor);
        ExcelUtil<SysAppTrialLogininfor> util = new ExcelUtil<SysAppTrialLogininfor>(SysAppTrialLogininfor.class);
        return util.exportExcel(list, "系统访问记录数据");
    }

    /**
     * 获取系统访问记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:appTrialLogininfor:query')")
    @GetMapping(value = "/{infoId}")
    public AjaxResult getInfo(@PathVariable("infoId") Long infoId) {
        return AjaxResult.success(sysAppTrialLogininforService.selectSysAppTrialLogininforByInfoId(infoId));
    }

    /**
     * 新增系统访问记录
     */
    @PreAuthorize("@ss.hasPermi('system:appTrialLogininfor:add')")
    @Log(title = "系统访问记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAppTrialLogininfor sysAppTrialLogininfor) {
        sysAppTrialLogininfor.setCreateBy(getUsername());
        return toAjax(sysAppTrialLogininforService.insertSysAppTrialLogininfor(sysAppTrialLogininfor));
    }

    /**
     * 修改系统访问记录
     */
    @PreAuthorize("@ss.hasPermi('system:appTrialLogininfor:edit')")
    @Log(title = "系统访问记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAppTrialLogininfor sysAppTrialLogininfor) {
        sysAppTrialLogininfor.setUpdateBy(getUsername());
        return toAjax(sysAppTrialLogininforService.updateSysAppTrialLogininfor(sysAppTrialLogininfor));
    }

    /**
     * 删除系统访问记录
     */
    @PreAuthorize("@ss.hasPermi('system:appTrialLogininfor:remove')")
    @Log(title = "系统访问记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public AjaxResult remove(@PathVariable Long[] infoIds) {
        return toAjax(sysAppTrialLogininforService.deleteSysAppTrialLogininforByInfoIds(infoIds));
    }

    @PreAuthorize("@ss.hasPermi('monitor:appTrialLogininfor:remove')")
    @Log(title = "系统访问记录", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public AjaxResult clean() {
        sysAppTrialLogininforService.cleanTrialLogininfor();
        return AjaxResult.success();
    }
}
