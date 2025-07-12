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
import com.ruoyi.device.domain.DeviceHour;
import com.ruoyi.device.service.IDeviceHourService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 设备时长Controller
 * 
 * @author auto
 * @date 2025-06-25
 */
@RestController
@RequestMapping("/device/hour")
public class DeviceHourController extends BaseController
{
    @Autowired
    private IDeviceHourService deviceHourService;

    /**
     * 查询设备时长列表
     */
    @PreAuthorize("@ss.hasPermi('device:hour:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeviceHour deviceHour)
    {
        startPage();
        List<DeviceHour> list = deviceHourService.selectDeviceHourList(deviceHour);
        return getDataTable(list);
    }

    /**
     * 导出设备时长列表
     */
    @PreAuthorize("@ss.hasPermi('device:hour:export')")
    @Log(title = "设备时长", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeviceHour deviceHour)
    {
        List<DeviceHour> list = deviceHourService.selectDeviceHourList(deviceHour);
        ExcelUtil<DeviceHour> util = new ExcelUtil<DeviceHour>(DeviceHour.class);
        util.exportExcel(response, list, "设备时长数据");
    }

    /**
     * 获取设备时长详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:hour:query')")
    @GetMapping(value = "/{deviceId}")
    public AjaxResult getInfo(@PathVariable("deviceId") Long deviceId)
    {
        return success(deviceHourService.selectDeviceHourByDeviceId(deviceId));
    }

    /**
     * 新增设备时长
     */
    @PreAuthorize("@ss.hasPermi('device:hour:add')")
    @Log(title = "设备时长", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeviceHour deviceHour)
    {
        return toAjax(deviceHourService.insertDeviceHour(deviceHour));
    }

    /**
     * 修改设备时长
     */
    @PreAuthorize("@ss.hasPermi('device:hour:edit')")
    @Log(title = "设备时长", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceHour deviceHour)
    {
        return toAjax(deviceHourService.updateDeviceHour(deviceHour));
    }

    /**
     * 删除设备时长
     */
    @PreAuthorize("@ss.hasPermi('device:hour:remove')")
    @Log(title = "设备时长", businessType = BusinessType.DELETE)
	@DeleteMapping("/{deviceIds}")
    public AjaxResult remove(@PathVariable Long[] deviceIds)
    {
        return toAjax(deviceHourService.deleteDeviceHourByDeviceIds(deviceIds));
    }
}
