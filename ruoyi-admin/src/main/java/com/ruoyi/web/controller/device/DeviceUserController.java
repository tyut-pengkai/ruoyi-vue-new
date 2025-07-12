package com.ruoyi.web.controller.device;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
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
import com.ruoyi.device.domain.DeviceUser;
import com.ruoyi.device.service.IDeviceUserService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 用户设备关联Controller
 * 
 * @author ruoyi
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/device/deviceUser")
public class DeviceUserController extends BaseController
{
    @Autowired
    private IDeviceUserService deviceUserService;

    /**
     * 查询用户设备关联列表
     */
    @PreAuthorize("@ss.hasPermi('device:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeviceUser deviceUser)
    {
        startPage();
        List<DeviceUser> list = deviceUserService.selectDeviceUserList(deviceUser);
        return getDataTable(list);
    }

    /**
     * 导出用户设备关联列表
     */
    @PreAuthorize("@ss.hasPermi('device:info:export')")
    @Log(title = "用户设备关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeviceUser deviceUser)
    {
        List<DeviceUser> list = deviceUserService.selectDeviceUserList(deviceUser);
        ExcelUtil<DeviceUser> util = new ExcelUtil<DeviceUser>(DeviceUser.class);
        util.exportExcel(response, list, "用户设备关联数据");
    }

    /**
     * 获取用户设备关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:info:query')")
    @GetMapping(value = "/{deviceId}")
    public AjaxResult getInfo(@PathVariable("deviceId") Long deviceId)
    {
        return success(deviceUserService.selectDeviceUserByDeviceId(deviceId));
    }

    /**
     * 新增用户设备关联
     */
    @PreAuthorize("@ss.hasPermi('device:info:add')")
    @Log(title = "用户设备关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeviceUser deviceUser)
    {
        return toAjax(deviceUserService.insertDeviceUser(deviceUser));
    }

    /**
     * 修改用户设备关联
     */
    @PreAuthorize("@ss.hasPermi('device:info:edit')")
    @Log(title = "用户设备关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceUser deviceUser)
    {
        return toAjax(deviceUserService.updateDeviceUser(deviceUser));
    }

    /**
     * 删除用户设备关联
     */
    @PreAuthorize("@ss.hasPermi('device:info:remove')")
    @Log(title = "用户设备关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{deviceIds}")
    public AjaxResult remove(@PathVariable Long[] deviceIds)
    {
        return toAjax(deviceUserService.deleteDeviceUserByDeviceIds(deviceIds));
    }
}
