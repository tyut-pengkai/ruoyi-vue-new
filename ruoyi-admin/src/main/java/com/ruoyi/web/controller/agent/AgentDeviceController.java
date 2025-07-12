package com.ruoyi.web.controller.agent;

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
import com.ruoyi.agent.domain.AgentDevice;
import com.ruoyi.agent.service.IAgentDeviceService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 智能体设备关联Controller
 * 
 * @author ruoyi
 * @date 2025-06-20
 */
@RestController
@RequestMapping("/agent/agentDevice")
public class AgentDeviceController extends BaseController
{
    @Autowired
    private IAgentDeviceService agentDeviceService;

    /**
     * 查询智能体设备关联列表
     */
    @PreAuthorize("@ss.hasPermi('agent:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgentDevice agentDevice)
    {
        startPage();
        List<AgentDevice> list = agentDeviceService.selectAgentDeviceList(agentDevice);
        return getDataTable(list);
    }

    /**
     * 导出智能体设备关联列表
     */
    @PreAuthorize("@ss.hasPermi('agent:info:export')")
    @Log(title = "智能体设备关联", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgentDevice agentDevice)
    {
        List<AgentDevice> list = agentDeviceService.selectAgentDeviceList(agentDevice);
        ExcelUtil<AgentDevice> util = new ExcelUtil<AgentDevice>(AgentDevice.class);
        util.exportExcel(response, list, "智能体设备关联数据");
    }

    /**
     * 获取智能体设备关联详细信息
     */
    @PreAuthorize("@ss.hasPermi('agent:info:query')")
    @GetMapping(value = "/{deviceId}")
    public AjaxResult getInfo(@PathVariable("deviceId") Long deviceId)
    {
        return success(agentDeviceService.selectAgentDeviceByDeviceId(deviceId));
    }

    /**
     * 新增智能体设备关联
     */
    @PreAuthorize("@ss.hasPermi('agent:info:add')")
    @Log(title = "智能体设备关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgentDevice agentDevice)
    {
        return toAjax(agentDeviceService.insertAgentDevice(agentDevice));
    }

    /**
     * 修改智能体设备关联
     */
    @PreAuthorize("@ss.hasPermi('agent:info:edit')")
    @Log(title = "智能体设备关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgentDevice agentDevice)
    {
        return toAjax(agentDeviceService.updateAgentDevice(agentDevice));
    }

    /**
     * 删除智能体设备关联
     */
    @PreAuthorize("@ss.hasPermi('agent:info:remove')")
    @Log(title = "智能体设备关联", businessType = BusinessType.DELETE)
	@DeleteMapping("/{deviceIds}")
    public AjaxResult remove(@PathVariable Long[] deviceIds)
    {
        return toAjax(agentDeviceService.deleteAgentDeviceByDeviceIds(deviceIds));
    }
}
