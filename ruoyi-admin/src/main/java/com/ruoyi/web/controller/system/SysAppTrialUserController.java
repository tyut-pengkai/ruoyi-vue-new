package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysAppTrialUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.ISysAppTrialUserService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 试用信息Controller
 *
 * @author zwgu
 * @date 2022-08-01
 */
@RestController
@RequestMapping("/system/appTrialUser")
public class SysAppTrialUserController extends BaseController {
    @Autowired
    private ISysAppTrialUserService sysAppTrialUserService;

    /**
     * 查询试用信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:appTrialUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysAppTrialUser sysAppTrialUser) {
        startPage();
        List<SysAppTrialUser> list = sysAppTrialUserService.selectSysAppTrialUserList(sysAppTrialUser);
        return getDataTable(list);
    }

    /**
     * 导出试用信息列表
     */
    @PreAuthorize("@ss.hasPermi('system:appTrialUser:export')")
    @Log(title = "试用信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysAppTrialUser sysAppTrialUser) {
        List<SysAppTrialUser> list = sysAppTrialUserService.selectSysAppTrialUserList(sysAppTrialUser);
        ExcelUtil<SysAppTrialUser> util = new ExcelUtil<SysAppTrialUser>(SysAppTrialUser.class);
        util.exportExcel(response, list, "试用信息数据");
    }

    /**
     * 获取试用信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:appTrialUser:query')")
    @GetMapping(value = "/{appTrialUserId}")
    public AjaxResult getInfo(@PathVariable("appTrialUserId") Long appTrialUserId) {
        return AjaxResult.success(sysAppTrialUserService.selectSysAppTrialUserByAppTrialUserId(appTrialUserId));
    }

    /**
     * 新增试用信息
     */
    @PreAuthorize("@ss.hasPermi('system:appTrialUser:add')")
    @Log(title = "试用信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysAppTrialUser sysAppTrialUser) {
        return toAjax(sysAppTrialUserService.insertSysAppTrialUser(sysAppTrialUser));
    }

    /**
     * 修改试用信息
     */
    @PreAuthorize("@ss.hasPermi('system:appTrialUser:edit')")
    @Log(title = "试用信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysAppTrialUser sysAppTrialUser) {
        return toAjax(sysAppTrialUserService.updateSysAppTrialUser(sysAppTrialUser));
    }

    /**
     * 删除试用信息
     */
    @PreAuthorize("@ss.hasPermi('system:appTrialUser:remove')")
    @Log(title = "试用信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{appTrialUserIds}")
    public AjaxResult remove(@PathVariable Long[] appTrialUserIds) {
        return toAjax(sysAppTrialUserService.deleteSysAppTrialUserByAppTrialUserIds(appTrialUserIds));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:appTrialUser:edit')")
    @Log(title = "试用信息", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysAppTrialUser sysAppTrialUser) {
        return toAjax(sysAppTrialUserService.updateSysAppTrialUser(sysAppTrialUser));
    }
}
