package com.ruoyi.agent.service.impl;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.domain.vo.AgentInfoVo;
import com.ruoyi.agent.mapper.SysAgentMapper;
import com.ruoyi.agent.service.ISysAgentService;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 代理管理Service业务层处理
 *
 * @author zwgu
 * @date 2022-06-11
 */
@Service
public class SysAgentServiceImpl implements ISysAgentService {
    @Autowired
    private SysAgentMapper sysAgentMapper;

    /**
     * 查询代理管理
     *
     * @param agentId 代理管理主键
     * @return 代理管理
     */
    @Override
    public SysAgent selectSysAgentByAgentId(Long agentId) {
        return sysAgentMapper.selectSysAgentByAgentId(agentId);
    }

    /**
     * 查询代理管理
     *
     * @param userId 代理管理主键
     * @return 代理管理
     */
    public SysAgent selectSysAgentByUserId(Long userId) {
        return sysAgentMapper.selectSysAgentByUserId(userId);
    }

    /**
     * 查询代理管理列表
     *
     * @param sysAgent 代理管理
     * @return 代理管理
     */
    @Override
    public List<SysAgent> selectSysAgentList(SysAgent sysAgent) {
        return sysAgentMapper.selectSysAgentList(sysAgent);
    }

    /**
     * 新增代理管理
     *
     * @param sysAgent 代理管理
     * @return 结果
     */
    @Override
    public int insertSysAgent(SysAgent sysAgent) {
        sysAgent.setCreateTime(DateUtils.getNowDate());
        return sysAgentMapper.insertSysAgent(sysAgent);
    }

    /**
     * 修改代理管理
     *
     * @param sysAgent 代理管理
     * @return 结果
     */
    @Override
    public int updateSysAgent(SysAgent sysAgent) {
        sysAgent.setUpdateTime(DateUtils.getNowDate());
        return sysAgentMapper.updateSysAgent(sysAgent);
    }

    /**
     * 批量删除代理管理
     *
     * @param agentIds 需要删除的代理管理主键
     * @return 结果
     */
    @Override
    public int deleteSysAgentByAgentIds(Long[] agentIds) {
        return sysAgentMapper.deleteSysAgentByAgentIds(agentIds);
    }

    /**
     * 删除代理管理信息
     *
     * @param agentId 代理管理主键
     * @return 结果
     */
    @Override
    public int deleteSysAgentByAgentId(Long agentId) {
        return sysAgentMapper.deleteSysAgentByAgentId(agentId);
    }

    /**
     * 获取非代理用户
     *
     * @return
     */
    public List<AgentInfoVo> getNonAgents() {
        return sysAgentMapper.getNonAgents();
    }

    /**
     * 获取代理的所有子代理
     *
     * @return
     */
    public List<Long> getSubAgents(Long agentId) {
        return sysAgentMapper.getSubAgents(agentId);
    }
}
