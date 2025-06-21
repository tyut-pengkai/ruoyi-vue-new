package com.ruoyi.agent.mapper;

import java.util.List;
import com.ruoyi.agent.domain.AgentInfo;

/**
 * 智能体信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-20
 */
public interface AgentInfoMapper 
{
    /**
     * 查询智能体信息
     * 
     * @param agentId 智能体信息主键
     * @return 智能体信息
     */
    public AgentInfo selectAgentInfoByAgentId(Long agentId);

    /**
     * 查询智能体信息列表
     * 
     * @param agentInfo 智能体信息
     * @return 智能体信息集合
     */
    public List<AgentInfo> selectAgentInfoList(AgentInfo agentInfo);

    /**
     * 新增智能体信息
     * 
     * @param agentInfo 智能体信息
     * @return 结果
     */
    public int insertAgentInfo(AgentInfo agentInfo);

    /**
     * 修改智能体信息
     * 
     * @param agentInfo 智能体信息
     * @return 结果
     */
    public int updateAgentInfo(AgentInfo agentInfo);

    /**
     * 删除智能体信息
     * 
     * @param agentId 智能体信息主键
     * @return 结果
     */
    public int deleteAgentInfoByAgentId(Long agentId);

    /**
     * 批量删除智能体信息
     * 
     * @param agentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteAgentInfoByAgentIds(Long[] agentIds);
}
