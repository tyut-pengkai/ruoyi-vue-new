package com.ruoyi.web.controller.system;

import java.util.List;

import com.ruoyi.common.core.domain.entity.SysUser;
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
import com.ruoyi.system.domain.SysApp;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 软件Controller
 *
 * @author zwgu
 * @date 2021-11-05
 */
@RestController
@RequestMapping("/system/app")
public class SysAppController extends BaseController
{
    @Autowired
    private ISysAppService sysAppService;

    /**
     * 查询软件列表
     */
    @PreAuthorize("@ss.hasPermi('system:app:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysApp sysApp)
    {
        startPage();
        List<SysApp> list = sysAppService.selectSysAppList(sysApp);
        return getDataTable(list);
    }

    /**
     * 导出软件列表
     */
    @PreAuthorize("@ss.hasPermi('system:app:export')")
    @Log(title = "软件管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysApp sysApp)
    {
        List<SysApp> list = sysAppService.selectSysAppList(sysApp);
        ExcelUtil<SysApp> util = new ExcelUtil<SysApp>(SysApp.class);
        return util.exportExcel(list, "软件数据");
    }

    /**
     * 获取软件详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:app:query')")
    @GetMapping(value = "/{appId}")
    public AjaxResult getInfo(@PathVariable("appId") Long appId)
    {
        return AjaxResult.success(sysAppService.selectSysAppByAppId(appId));
    }

    /**
     * 新增软件
     */
    @PreAuthorize("@ss.hasPermi('system:app:add')")
    @Log(title = "软件管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysApp sysApp)
    {
        return toAjax(sysAppService.insertSysApp(sysApp));
    }

    /**
     * 修改软件
     */
    @PreAuthorize("@ss.hasPermi('system:app:edit')")
    @Log(title = "软件管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysApp sysApp)
    {
        return toAjax(sysAppService.updateSysApp(sysApp));
    }

    /**
     * 删除软件
     */
    @PreAuthorize("@ss.hasPermi('system:app:remove')")
    @Log(title = "软件管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{appIds}")
    public AjaxResult remove(@PathVariable Long[] appIds)
    {
        return toAjax(sysAppService.deleteSysAppByAppIds(appIds));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:app:edit')")
    @Log(title = "软件管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysApp app)
    {
        app.setUpdateBy(getUsername());
        return toAjax(sysAppService.updateSysAppStatus(app));
    }

    /**
     * 计费状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:app:edit')")
    @Log(title = "软件管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeChargeStatus")
    public AjaxResult changeChargeStatus(@RequestBody SysApp app)
    {
        app.setUpdateBy(getUsername());
        return toAjax(sysAppService.updateSysAppChargeStatus(app));
    }
}
