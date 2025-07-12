package com.ruoyi.agent.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 智能体设备关联对象 agent_device
 * 
 * @author ruoyi
 * @date 2025-06-20
 */
public class AgentDevice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 智能体id */
    @Excel(name = "智能体id")
    private Long agentId;

    /** 设备id */
    @Excel(name = "设备id")
    private Long deviceId;

    public void setAgentId(Long agentId) 
    {
        this.agentId = agentId;
    }

    public Long getAgentId() 
    {
        return agentId;
    }

    public void setDeviceId(Long deviceId) 
    {
        this.deviceId = deviceId;
    }

    public Long getDeviceId() 
    {
        return deviceId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("agentId", getAgentId())
            .append("deviceId", getDeviceId())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
