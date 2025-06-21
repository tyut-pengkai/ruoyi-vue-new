package com.ruoyi.agent.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.agent.mapper.AgentDeviceMapper;
import com.ruoyi.agent.domain.AgentDevice;
import com.ruoyi.agent.service.IAgentDeviceService;

/**
 * 智能体设备关联Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-20
 */
@Service
public class AgentDeviceServiceImpl implements IAgentDeviceService 
{
    @Autowired
    private AgentDeviceMapper agentDeviceMapper;

    /**
     * 查询智能体设备关联
     * 
     * @param deviceId 智能体设备关联主键
     * @return 智能体设备关联
     */
    @Override
    public AgentDevice selectAgentDeviceByDeviceId(Long deviceId)
    {
        return agentDeviceMapper.selectAgentDeviceByDeviceId(deviceId);
    }

    /**
     * 查询智能体设备关联列表
     * 
     * @param agentDevice 智能体设备关联
     * @return 智能体设备关联
     */
    @Override
    public List<AgentDevice> selectAgentDeviceList(AgentDevice agentDevice)
    {
        return agentDeviceMapper.selectAgentDeviceList(agentDevice);
    }

    /**
     * 新增智能体设备关联
     * 
     * @param agentDevice 智能体设备关联
     * @return 结果
     */
    @Override
    public int insertAgentDevice(AgentDevice agentDevice)
    {
        agentDevice.setCreateTime(DateUtils.getNowDate());
        return agentDeviceMapper.insertAgentDevice(agentDevice);
    }

    /**
     * 修改智能体设备关联
     * 
     * @param agentDevice 智能体设备关联
     * @return 结果
     */
    @Override
    public int updateAgentDevice(AgentDevice agentDevice)
    {
        agentDevice.setUpdateTime(DateUtils.getNowDate());
        return agentDeviceMapper.updateAgentDevice(agentDevice);
    }

    /**
     * 批量删除智能体设备关联
     * 
     * @param deviceIds 需要删除的智能体设备关联主键
     * @return 结果
     */
    @Override
    public int deleteAgentDeviceByDeviceIds(Long[] deviceIds)
    {
        return agentDeviceMapper.deleteAgentDeviceByDeviceIds(deviceIds);
    }

    /**
     * 删除智能体设备关联信息
     * 
     * @param deviceId 智能体设备关联主键
     * @return 结果
     */
    @Override
    public int deleteAgentDeviceByDeviceId(Long deviceId)
    {
        return agentDeviceMapper.deleteAgentDeviceByDeviceId(deviceId);
    }
}
