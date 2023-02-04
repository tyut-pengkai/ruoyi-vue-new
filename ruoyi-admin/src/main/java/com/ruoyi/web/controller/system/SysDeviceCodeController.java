package com.ruoyi.web.controller.system;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDeviceCode;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.service.ISysDeviceCodeService;
import com.ruoyi.utils.poi.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备码管理Controller
 *
 * @author zwgu
 * @date 2021-12-06
 */
@RestController
@RequestMapping("/system/deviceCode")
public class SysDeviceCodeController extends BaseController
{
    @Autowired
    private ISysDeviceCodeService sysDeviceCodeService;

    /**
     * 查询设备码管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:deviceCode:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysDeviceCode sysDeviceCode)
    {
        startPage();
        List<SysDeviceCode> list = sysDeviceCodeService.selectSysDeviceCodeList(sysDeviceCode);
        return getDataTable(list);
    }

    /**
     * 导出设备码管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:deviceCode:export')")
    @Log(title = "设备码管理", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public AjaxResult export(SysDeviceCode sysDeviceCode)
    {
        List<SysDeviceCode> list = sysDeviceCodeService.selectSysDeviceCodeList(sysDeviceCode);
        ExcelUtil<SysDeviceCode> util = new ExcelUtil<SysDeviceCode>(SysDeviceCode.class);
        return util.exportExcel(list, "设备码管理数据");
    }

    /**
     * 获取设备码管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:deviceCode:query')")
    @GetMapping(value = "/{deviceCodeId}")
    public AjaxResult getInfo(@PathVariable("deviceCodeId") Long deviceCodeId)
    {
        return AjaxResult.success(sysDeviceCodeService.selectSysDeviceCodeByDeviceCodeId(deviceCodeId));
    }

    /**
     * 新增设备码管理
     */
    @PreAuthorize("@ss.hasPermi('system:deviceCode:add')")
    @Log(title = "设备码管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysDeviceCode sysDeviceCode)
    {
        sysDeviceCode.setCreateBy(getUsername());
        return toAjax(sysDeviceCodeService.insertSysDeviceCode(sysDeviceCode));
    }

    /**
     * 修改设备码管理
     */
    @PreAuthorize("@ss.hasPermi('system:deviceCode:edit')")
    @Log(title = "设备码管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysDeviceCode sysDeviceCode)
    {
        sysDeviceCode.setUpdateBy(getUsername());
        return toAjax(sysDeviceCodeService.updateSysDeviceCode(sysDeviceCode));
    }

    /**
     * 删除设备码管理
     */
    @PreAuthorize("@ss.hasPermi('system:deviceCode:remove')")
    @Log(title = "设备码管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deviceCodeIds}")
    public AjaxResult remove(@PathVariable Long[] deviceCodeIds)
    {
        return toAjax(sysDeviceCodeService.deleteSysDeviceCodeByDeviceCodeIds(deviceCodeIds));
    }

    /**
     * 状态修改
     */
    @PreAuthorize("@ss.hasPermi('system:deviceCode:edit')")
    @Log(title = "设备码管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody SysDeviceCode deviceCode)
    {
        deviceCode.setUpdateBy(getUsername());
        return toAjax(sysDeviceCodeService.updateSysDeviceCodeStatus(deviceCode));
    }
}
