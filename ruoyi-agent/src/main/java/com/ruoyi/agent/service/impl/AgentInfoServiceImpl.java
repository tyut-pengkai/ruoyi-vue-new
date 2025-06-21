package com.ruoyi.agent.service.impl;

import java.util.List;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.agent.mapper.AgentInfoMapper;
import com.ruoyi.agent.domain.AgentInfo;
import com.ruoyi.agent.service.IAgentInfoService;

/**
 * 智能体信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-20
 */
@Service
public class AgentInfoServiceImpl implements IAgentInfoService 
{
    @Autowired
    private AgentInfoMapper agentInfoMapper;

    /**
     * 查询智能体信息
     * 
     * @param agentId 智能体信息主键
     * @return 智能体信息
     */
    @Override
    public AgentInfo selectAgentInfoByAgentId(Long agentId)
    {
        return agentInfoMapper.selectAgentInfoByAgentId(agentId);
    }

    /**
     * 查询智能体信息列表
     * 
     * @param agentInfo 智能体信息
     * @return 智能体信息
     */
    @Override
    public List<AgentInfo> selectAgentInfoList(AgentInfo agentInfo)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (loginUser != null && !loginUser.getUser().isAdmin()) {
            agentInfo.getParams().put("currentUserId", loginUser.getUserId());
        }
        return agentInfoMapper.selectAgentInfoList(agentInfo);
    }

    /**
     * 新增智能体信息
     * 
     * @param agentInfo 智能体信息
     * @return 结果
     */
    @Override
    public int insertAgentInfo(AgentInfo agentInfo)
    {
        return agentInfoMapper.insertAgentInfo(agentInfo);
    }

    /**
     * 修改智能体信息
     * 
     * @param agentInfo 智能体信息
     * @return 结果
     */
    @Override
    public int updateAgentInfo(AgentInfo agentInfo)
    {
        return agentInfoMapper.updateAgentInfo(agentInfo);
    }

    /**
     * 批量删除智能体信息
     * 
     * @param agentIds 需要删除的智能体信息主键
     * @return 结果
     */
    @Override
    public int deleteAgentInfoByAgentIds(Long[] agentIds)
    {
        return agentInfoMapper.deleteAgentInfoByAgentIds(agentIds);
    }

    /**
     * 删除智能体信息信息
     * 
     * @param agentId 智能体信息主键
     * @return 结果
     */
    @Override
    public int deleteAgentInfoByAgentId(Long agentId)
    {
        return agentInfoMapper.deleteAgentInfoByAgentId(agentId);
    }
}
