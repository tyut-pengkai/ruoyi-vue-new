package com.ruoyi.web.controller.system;

import com.ruoyi.api.v1.utils.MyUtils;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysApp;
import com.ruoyi.common.core.domain.entity.SysAppUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.AuthType;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.service.ISysAppService;
import com.ruoyi.system.service.ISysAppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 软件用户Controller
 *
 * @author zwgu
 * @date 2021-11-09
 */
@RestController
@RequestMapping("/system/appUser")
public class SysAppUserController extends BaseController {
    @Autowired
    private ISysAppUserService sysAppUserService;
    @Autowired
    private ISysAppService sysAppService;

    /**
     * 查询软件用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:appUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysAppUser sysAppUser) {
        startPage();
        List<SysAppUser> list = sysAppUserService.selectSysAppUserList(sysAppUser);
        for (SysAppUser sau : list) {
            sau.setEffectiveLoginLimitU(MyUtils.getEffectiveLoginLimitU(sau));
            sau.setEffectiveLoginLimitM(MyUtils.getEffectiveLoginLimitM(sau));
        }
        return getDataTable(list);
    }

    /**
     * 导出软件用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:appUser:export')")
    @Log(title = "软件用户", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysAppUser sysAppUser) {
        List<SysAppUser> list = sysAppUserService.selectSysAppUserList(sysAppUser);
        ExcelUtil<SysAppUser> util = new ExcelUtil<SysAppUser>(SysAppUser.class);
        return util.exportExcel(list, "软件用户数据");
    }

    /**
     * 获取软件用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:appUser:query')")
    @GetMapping(value = "/{appUserId}")
    public AjaxResult getInfo(@PathVariable("appUserId") Long appUserId) {
        return AjaxResult.success(sysAppUserService.selectSysAppUserByAppUserId(appUserId));
    }

    /**
     * 新增软件用户
     */
    @PreAuthorize("@ss.hasPermi('system:appUser:add')")
    @Log(title = "软件用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAppUser sysAppUser) {
        sysAppUser.setCreateBy(getUsername());
        SysApp sysApp = sysAppService.selectSysAppByAppId(sysAppUser.getAppId());
        if (sysApp.getAuthType() == AuthType.ACCOUNT) {
            SysAppUser appUser = sysAppUserService.selectSysAppUserByAppIdAndUserId(sysAppUser.getAppId(), sysAppUser.getUserId());
            if (appUser != null) {
                return AjaxResult.error("当前软件下该账号已拥有软件用户，可直接登录，无需重复添加");
            }
        } else {
            SysAppUser appUser = sysAppUserService.selectSysAppUserByAppIdAndLoginCode(sysAppUser.getAppId(), sysAppUser.getLoginCode());
            if (appUser != null) {
                return AjaxResult.error("当前软件下该单码已拥有软件用户，可直接登录，无需重复添加");
            }
        }
        return toAjax(sysAppUserService.insertSysAppUser(sysAppUser));
    }

    /**
     * 修改软件用户
     */
    @PreAuthorize("@ss.hasPermi('system:appUser:edit')")
    @Log(title = "软件用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAppUser sysAppUser) {
        sysAppUser.setUpdateBy(getUsername());
        return toAjax(sysAppUserService.updateSysAppUser(sysAppUser));
    }

    /**
     * 删除软件用户
     */
    @PreAuthorize("@ss.hasPermi('system:appUser:remove')")
    @Log(title = "软件用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{appUserIds}")
    public AjaxResult remove(@PathVariable Long[] appUserIds) {
        return toAjax(sysAppUserService.deleteSysAppUserByAppUserIds(appUserIds));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:appUser:edit')")
    @Log(title = "设备码管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysAppUser sysAppUser) {
        sysAppUser.setUpdateBy(getUsername());
        return toAjax(sysAppUserService.updateSysDeviceCodeStatus(sysAppUser));
    }
}
