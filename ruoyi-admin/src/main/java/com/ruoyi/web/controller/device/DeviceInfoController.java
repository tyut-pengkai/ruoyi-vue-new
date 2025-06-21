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
import com.ruoyi.device.domain.DeviceInfo;
import com.ruoyi.device.service.IDeviceInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.SecurityUtils;
import java.util.Map;
import com.ruoyi.device.service.IDeviceUserService;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Anonymous;
import com.ruoyi.agent.domain.AgentDevice;
import com.ruoyi.agent.service.IAgentDeviceService;

/**
 * 设备信息Controller
 * 
 * @author ruoyi
 * @date 2025-06-18
 */
@RestController
@RequestMapping("/device/info")
public class DeviceInfoController extends BaseController
{
    @Autowired
    private IDeviceInfoService deviceInfoService;

    @Autowired
    private IDeviceUserService deviceUserService;

    @Autowired
    private IAgentDeviceService agentDeviceService;

    /**
     * 查询设备信息列表
     */
    @PreAuthorize("@ss.hasPermi('device:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(DeviceInfo deviceInfo)
    {
        startPage();
        Long userId = SecurityUtils.getUserId();
        boolean isAdmin = SecurityUtils.isAdmin(userId);
        List<DeviceInfo> list = deviceInfoService.selectDeviceInfoListByUser(userId, isAdmin, deviceInfo);
        return getDataTable(list);
    }

    /**
     * 导出设备信息列表
     */
    @PreAuthorize("@ss.hasPermi('device:info:export')")
    @Log(title = "设备信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, DeviceInfo deviceInfo)
    {
        List<DeviceInfo> list = deviceInfoService.selectDeviceInfoList(deviceInfo);
        ExcelUtil<DeviceInfo> util = new ExcelUtil<DeviceInfo>(DeviceInfo.class);
        util.exportExcel(response, list, "设备信息数据");
    }

    /**
     * 获取设备信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('device:info:query')")
    @GetMapping(value = "/{deviceId}")
    public AjaxResult getInfo(@PathVariable("deviceId") Long deviceId)
    {
        return success(deviceInfoService.selectDeviceInfoByDeviceId(deviceId));
    }

    /**
     * 新增设备信息
     */
    @PreAuthorize("@ss.hasPermi('device:info:add')")
    @Log(title = "设备信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody DeviceInfo deviceInfo)
    {
        return toAjax(deviceInfoService.insertDeviceInfo(deviceInfo));
    }

    /**
     * 修改设备信息
     */
    @PreAuthorize("@ss.hasPermi('device:info:edit')")
    @Log(title = "设备信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody DeviceInfo deviceInfo)
    {
        return toAjax(deviceInfoService.updateDeviceInfo(deviceInfo));
    }

    /**
     * 绑定智能体到设备
     */
    @Log(title = "设备信息", businessType = BusinessType.UPDATE)
    @PutMapping("/bind-agent")
    public AjaxResult bindAgent(@RequestBody AgentDevice agentDevice)
    {
        return toAjax(agentDeviceService.updateAgentDevice(agentDevice));
    }

    /**
     * 删除设备信息
     */
    @PreAuthorize("@ss.hasPermi('device:info:remove')")
    @Log(title = "设备信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{deviceIds}")
    public AjaxResult remove(@PathVariable Long[] deviceIds)
    {
        return toAjax(deviceInfoService.deleteDeviceInfoByDeviceIds(deviceIds));
    }

    @PreAuthorize("@ss.hasPermi('device:info:bindToUser')")
    @PostMapping("/bindDeviceToUser")
    public AjaxResult bindDevice(@RequestBody Map<String, String> params) {
        Long userId = SecurityUtils.getUserId();
        String codeOrMac = params.get("deviceMacCode");
        DeviceInfo device = deviceInfoService.selectByCodeOrMac(codeOrMac, codeOrMac);
        if (device == null) {
            return AjaxResult.error("设备不存在");
        }
        int result = deviceUserService.bindDeviceToUser(userId, device.getDeviceId());
        if (result == -1) {
            return AjaxResult.error("该设备已被其他用户绑定");
        }
        return result > 0 ? AjaxResult.success() : AjaxResult.error("绑定失败或已绑定");
    }

    @PreAuthorize("@ss.hasPermi('device:info:unbindToUser')")
    @PostMapping("/unbindDeviceToUser")
    public AjaxResult unbindDeviceToUser(@RequestBody Map<String, Long> params) {
        Long userId = SecurityUtils.getUserId();
        Long deviceId = params.get("deviceId");
        int result = deviceUserService.unbindDeviceFromUser(userId, deviceId);
        return result > 0 ? AjaxResult.success() : AjaxResult.error("解绑失败");
    }

    @Log(title = "设备信息", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('device:info:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<DeviceInfo> util = new ExcelUtil<DeviceInfo>(DeviceInfo.class);
        List<DeviceInfo> deviceList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = deviceInfoService.importDevice(deviceList, updateSupport, operName);
        return success(message);
    }

    @PreAuthorize("@ss.hasPermi('device:info:import')")
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<DeviceInfo> util = new ExcelUtil<DeviceInfo>(DeviceInfo.class);
        util.importTemplateExcel(response, "设备数据");
    }
}
