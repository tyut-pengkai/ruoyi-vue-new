package com.ruoyi.agent.mapper;

import java.util.List;
import com.ruoyi.agent.domain.AgentDevice;

/**
 * 智能体设备关联Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-20
 */
public interface AgentDeviceMapper 
{
    /**
     * 查询智能体设备关联
     * 
     * @param deviceId 智能体设备关联主键
     * @return 智能体设备关联
     */
    public AgentDevice selectAgentDeviceByDeviceId(Long deviceId);

    /**
     * 查询智能体设备关联列表
     * 
     * @param agentDevice 智能体设备关联
     * @return 智能体设备关联集合
     */
    public List<AgentDevice> selectAgentDeviceList(AgentDevice agentDevice);

    /**
     * 新增智能体设备关联
     * 
     * @param agentDevice 智能体设备关联
     * @return 结果
     */
    public int insertAgentDevice(AgentDevice agentDevice);

    /**
     * 修改智能体设备关联
     * 
     * @param agentDevice 智能体设备关联
     * @return 结果
     */
    public int updateAgentDevice(AgentDevice agentDevice);

    /**
     * 删除智能体设备关联
     * 
     * @param deviceId 智能体设备关联主键
     * @return 结果
     */
    public int deleteAgentDeviceByDeviceId(Long deviceId);

    /**
     * 批量删除智能体设备关联
     * 
     * @param deviceIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAgentDeviceByDeviceIds(Long[] deviceIds);
}
