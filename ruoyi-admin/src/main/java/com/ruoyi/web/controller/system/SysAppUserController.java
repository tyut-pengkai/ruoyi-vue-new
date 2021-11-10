package com.ruoyi.web.controller.system;

import java.util.List;
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
import com.ruoyi.system.domain.SysAppUser;
import com.ruoyi.system.service.ISysAppUserService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 软件用户Controller
 * 
 * @author zwgu
 * @date 2021-11-09
 */
@RestController
@RequestMapping("/system/app_user")
public class SysAppUserController extends BaseController
{
    @Autowired
    private ISysAppUserService sysAppUserService;

    /**
     * 查询软件用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:app_user:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysAppUser sysAppUser)
    {
        startPage();
        List<SysAppUser> list = sysAppUserService.selectSysAppUserList(sysAppUser);
        return getDataTable(list);
    }

    /**
     * 导出软件用户列表
     */
    @PreAuthorize("@ss.hasPermi('system:app_user:export')")
    @Log(title = "软件用户", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysAppUser sysAppUser)
    {
        List<SysAppUser> list = sysAppUserService.selectSysAppUserList(sysAppUser);
        ExcelUtil<SysAppUser> util = new ExcelUtil<SysAppUser>(SysAppUser.class);
        return util.exportExcel(list, "软件用户数据");
    }

    /**
     * 获取软件用户详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:app_user:query')")
    @GetMapping(value = "/{appUserId}")
    public AjaxResult getInfo(@PathVariable("appUserId") Long appUserId)
    {
        return AjaxResult.success(sysAppUserService.selectSysAppUserByAppUserId(appUserId));
    }

    /**
     * 新增软件用户
     */
    @PreAuthorize("@ss.hasPermi('system:app_user:add')")
    @Log(title = "软件用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAppUser sysAppUser)
    {
        return toAjax(sysAppUserService.insertSysAppUser(sysAppUser));
    }

    /**
     * 修改软件用户
     */
    @PreAuthorize("@ss.hasPermi('system:app_user:edit')")
    @Log(title = "软件用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAppUser sysAppUser)
    {
        return toAjax(sysAppUserService.updateSysAppUser(sysAppUser));
    }

    /**
     * 删除软件用户
     */
    @PreAuthorize("@ss.hasPermi('system:app_user:remove')")
    @Log(title = "软件用户", businessType = BusinessType.DELETE)
	@DeleteMapping("/{appUserIds}")
    public AjaxResult remove(@PathVariable Long[] appUserIds)
    {
        return toAjax(sysAppUserService.deleteSysAppUserByAppUserIds(appUserIds));
    }
}
