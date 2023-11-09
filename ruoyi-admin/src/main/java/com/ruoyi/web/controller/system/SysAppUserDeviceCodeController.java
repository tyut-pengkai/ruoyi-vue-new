package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.domain.entity.SysAppUserDeviceCode;
import com.ruoyi.common.core.domain.entity.SysDeviceCode;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BillType;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.enums.UnbindType;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.system.domain.SysUnbindLog;
import com.ruoyi.system.service.ISysAppUserDeviceCodeService;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.system.service.ISysDeviceCodeService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 软件用户与设备码关联Controller
 *
 * @author zwgu
 * @date 2021-12-18
 */
@RestController
@RequestMapping("/system/appUserDeviceCode")
public class SysAppUserDeviceCodeController extends BaseController {
    @Autowired
    private ISysAppUserDeviceCodeService appUserDeviceCodeService;
    @Autowired
    private ISysAppUserService appUserService;
    @Autowired
    private ISysDeviceCodeService deviceCodeService;

    /**
     * 查询软件用户与设备码关联列表
     */
    @PreAuthorize("@ss.hasPermi('system:appUserDeviceCode:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysAppUserDeviceCode sysAppUserDeviceCode) {
        startPage();
        List<SysAppUserDeviceCode> list = appUserDeviceCodeService.selectSysAppUserDeviceCodeList(sysAppUserDeviceCode);
        for (SysAppUserDeviceCode item : list) {
            item.setAppUser(appUserService.selectSysAppUserByAppUserId(item.getAppUserId()));
            item.setDeviceCode(deviceCodeService.selectSysDeviceCodeByDeviceCodeId(item.getDeviceCodeId()));
        }
        return getDataTable(list);
    }

    /**
     * 导出软件用户与设备码关联列表
     */
    @PreAuthorize("@ss.hasPermi('system:appUserDeviceCode:export')")
    @Log(title = "软件用户与设备码关联", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysAppUserDeviceCode sysAppUserDeviceCode) {
        List<SysAppUserDeviceCode> list = appUserDeviceCodeService.selectSysAppUserDeviceCodeList(sysAppUserDeviceCode);
        ExcelUtil<SysAppUserDeviceCode> util = new ExcelUtil<SysAppUserDeviceCode>(SysAppUserDeviceCode.class);
        return util.exportExcel(list, "软件用户与设备码关联数据");
    }

    /**
     * 获取软件用户与设备码关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:appUserDeviceCode:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        SysAppUserDeviceCode sysAppUserDeviceCode = appUserDeviceCodeService.selectSysAppUserDeviceCodeById(id);
        sysAppUserDeviceCode.setAppUser(appUserService.selectSysAppUserByAppUserId(sysAppUserDeviceCode.getAppUserId()));
        sysAppUserDeviceCode.setDeviceCode(deviceCodeService.selectSysDeviceCodeByDeviceCodeId(sysAppUserDeviceCode.getDeviceCodeId()));
        return AjaxResult.success(sysAppUserDeviceCode);
    }

    /**
     * 新增软件用户与设备码关联
     */
    @PreAuthorize("@ss.hasPermi('system:appUserDeviceCode:add')")
    @Log(title = "软件用户与设备码关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAppUserDeviceCode sysAppUserDeviceCode) {
        sysAppUserDeviceCode.setCreateBy(getUsername());
        return toAjax(appUserDeviceCodeService.insertSysAppUserDeviceCode(sysAppUserDeviceCode));
    }

    /**
     * 修改软件用户与设备码关联
     */
    @PreAuthorize("@ss.hasPermi('system:appUserDeviceCode:edit')")
    @Log(title = "软件用户与设备码关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAppUserDeviceCode sysAppUserDeviceCode) {
        sysAppUserDeviceCode.setUpdateBy(getUsername());
        return toAjax(appUserDeviceCodeService.updateSysAppUserDeviceCode(sysAppUserDeviceCode));
    }

    /**
     * 删除软件用户与设备码关联
     */
    @PreAuthorize("@ss.hasPermi('system:appUserDeviceCode:remove')")
    @Log(title = "软件用户与设备码关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        List<SysUnbindLog> list = new ArrayList<>();
        for (Long id : ids) {
            SysAppUserDeviceCode appUserDeviceCode = appUserDeviceCodeService.selectSysAppUserDeviceCodeById(id);
            SysDeviceCode deviceCode = deviceCodeService.selectSysDeviceCodeByDeviceCodeId(appUserDeviceCode.getDeviceCodeId());
            SysAppUser appUser = appUserService.selectSysAppUserByAppUserId(appUserDeviceCode.getAppUserId());
            // 解绑日志
            SysUnbindLog unbindLog = new SysUnbindLog();
            unbindLog.setAppUserId(appUserDeviceCode.getAppUserId());
            unbindLog.setAppId(appUser.getAppId());
            unbindLog.setFirstLoginTime(appUserDeviceCode.getCreateTime());
            unbindLog.setLastLoginTime(appUserDeviceCode.getLastLoginTime());
            unbindLog.setLoginTimes(appUserDeviceCode.getLoginTimes());
            unbindLog.setUnbindType(UnbindType.ADMIN_UNBIND);
            unbindLog.setUnbindDesc("管理员后台解绑");
            unbindLog.setDeviceCode(deviceCode.getDeviceCode());
            unbindLog.setDeviceCodeId(appUserDeviceCode.getDeviceCodeId());
            unbindLog.setChangeAmount(0L);
            if (appUser.getApp().getBillType() == BillType.TIME) {
                unbindLog.setExpireTimeAfter(appUser.getExpireTime());
                unbindLog.setExpireTimeBefore(appUser.getExpireTime());
            }
            if (appUser.getApp().getBillType() == BillType.POINT) {
                unbindLog.setPointAfter(appUser.getPoint());
                unbindLog.setPointBefore(appUser.getPoint());
            }
            list.add(unbindLog);
        }
        int i = appUserDeviceCodeService.deleteSysAppUserDeviceCodeByIds(ids);
        for (SysUnbindLog unbindLog : list) {
            AsyncManager.me().execute(AsyncFactory.recordDeviceUnbind(unbindLog));
        }
        return toAjax(i);
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:appUserDeviceCode:edit')")
    @Log(title = "设备码管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysAppUserDeviceCode appUserDeviceCode) {
        appUserDeviceCode.setUpdateBy(getUsername());
        return toAjax(appUserDeviceCodeService.updateSysDeviceCodeStatus(appUserDeviceCode));
    }
}
