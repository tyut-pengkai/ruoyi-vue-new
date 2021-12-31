package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.ISysAppService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        for (SysApp app : list) {
            setApiUrl(app);
        }
        return getDataTable(list);
    }

    /**
     * 导出软件列表
     */
    @PreAuthorize("@ss.hasPermi('system:app:export')")
    @Log(title = "软件管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysApp sysApp) {
        List<SysApp> list = sysAppService.selectSysAppList(sysApp);
        for (SysApp app : list) {
            setApiUrl(app);
        }
        ExcelUtil<SysApp> util = new ExcelUtil<SysApp>(SysApp.class);
        return util.exportExcel(list, "软件数据");
    }

    /**
     * 获取软件详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:app:query')")
    @GetMapping(value = "/{appId}")
    public AjaxResult getInfo(@PathVariable("appId") Long appId) {
        SysApp sysApp = sysAppService.selectSysAppByAppId(appId);
        setApiUrl(sysApp);
        return AjaxResult.success(sysApp);
    }

    /**
     * 新增软件
     */
    @PreAuthorize("@ss.hasPermi('system:app:add')")
    @Log(title = "软件管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysApp sysApp)
    {
        if (UserConstants.NOT_UNIQUE.equals(sysAppService.checkAppNameUnique(sysApp.getAppName(), null)))
        {
            return AjaxResult.error("新增软件'" + sysApp.getAppName() + "'失败，软件名称已存在");
        }
        sysApp.setCreateBy(getUsername());
        sysApp.setCreateTime(DateUtils.getNowDate());
        sysApp.setAppKey(RandomStringUtils.randomAlphanumeric(32));
        sysApp.setAppSecret(RandomStringUtils.randomAlphanumeric(32));
        setApiUrl(sysApp);
        sysApp.setDelFlag("0");
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
        if (UserConstants.NOT_UNIQUE.equals(sysAppService.checkAppNameUnique(sysApp.getAppName(), sysApp.getAppId())))
        {
            return AjaxResult.error("修改软件'" + sysApp.getAppName() + "'失败，软件名称已存在");
        }
        sysApp.setUpdateBy(getUsername());
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
    public AjaxResult changeChargeStatus(@RequestBody SysApp app) {
        app.setUpdateBy(getUsername());
        return toAjax(sysAppService.updateSysAppChargeStatus(app));
    }

    public void setApiUrl(SysApp app) {
        HttpServletRequest request = ServletUtils.getRequest();
        app.setApiUrl(request.getScheme() + "://" + request.getServerName()
                + ("80".equals(String.valueOf(request.getServerPort())) ? "" : ":" + request.getServerPort())
                + "/api/v1/" + app.getAppKey() + "/");
    }
}
