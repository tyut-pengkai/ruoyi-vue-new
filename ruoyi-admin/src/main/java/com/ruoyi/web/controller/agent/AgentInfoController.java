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
import com.ruoyi.agent.domain.AgentInfo;
import com.ruoyi.agent.service.IAgentInfoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 智能体信息Controller
 * 
 * @author ruoyi
 * @date 2025-06-20
 */
@RestController
@RequestMapping("/agent/info")
public class AgentInfoController extends BaseController
{
    @Autowired
    private IAgentInfoService agentInfoService;

    /**
     * 查询智能体信息列表
     */
    @PreAuthorize("@ss.hasPermi('agent:info:list')")
    @GetMapping("/list")
    public TableDataInfo list(AgentInfo agentInfo)
    {
        startPage();
        List<AgentInfo> list = agentInfoService.selectAgentInfoList(agentInfo);
        return getDataTable(list);
    }

    /**
     * 导出智能体信息列表
     */
    @PreAuthorize("@ss.hasPermi('agent:info:export')")
    @Log(title = "智能体信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AgentInfo agentInfo)
    {
        List<AgentInfo> list = agentInfoService.selectAgentInfoList(agentInfo);
        ExcelUtil<AgentInfo> util = new ExcelUtil<AgentInfo>(AgentInfo.class);
        util.exportExcel(response, list, "智能体信息数据");
    }

    /**
     * 获取智能体信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('agent:info:query')")
    @GetMapping(value = "/{agentId}")
    public AjaxResult getInfo(@PathVariable("agentId") Long agentId)
    {
        return success(agentInfoService.selectAgentInfoByAgentId(agentId));
    }

    /**
     * 新增智能体信息
     */
    @PreAuthorize("@ss.hasPermi('agent:info:add')")
    @Log(title = "智能体信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody AgentInfo agentInfo)
    {
        return toAjax(agentInfoService.insertAgentInfo(agentInfo));
    }

    /**
     * 修改智能体信息
     */
    @PreAuthorize("@ss.hasPermi('agent:info:edit')")
    @Log(title = "智能体信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody AgentInfo agentInfo)
    {
        return toAjax(agentInfoService.updateAgentInfo(agentInfo));
    }

    /**
     * 删除智能体信息
     */
    @PreAuthorize("@ss.hasPermi('agent:info:remove')")
    @Log(title = "智能体信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{agentIds}")
    public AjaxResult remove(@PathVariable Long[] agentIds)
    {
        return toAjax(agentInfoService.deleteAgentInfoByAgentIds(agentIds));
    }
}
