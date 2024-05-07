package com.ruoyi.agent.service;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.domain.vo.AgentInfoVo;
import com.ruoyi.common.core.domain.entity.SysUser;

import java.util.List;
import java.util.Set;

/**
 * 代理管理Service接口
 *
 * @author zwgu
 * @date 2022-06-11
 */
public interface ISysAgentUserService {
    /**
     * 查询代理管理
     *
     * @param agentId 代理管理主键
     * @return 代理管理
     */
    public SysAgent selectSysAgentByAgentId(Long agentId);

    /**
     * 查询代理管理
     *
     * @param userId 代理管理主键
     * @return 代理管理
     */
    public SysAgent selectSysAgentByUserId(Long userId);

    /**
     * 查询代理管理列表
     *
     * @param sysAgent 代理管理
     * @return 代理管理集合
     */
    public List<SysAgent> selectSysAgentList(SysAgent sysAgent);

    /**
     * 新增代理管理
     *
     * @param sysAgent 代理管理
     * @return 结果
     */
    public int insertSysAgent(SysAgent sysAgent);

    /**
     * 修改代理管理
     *
     * @param sysAgent 代理管理
     * @return 结果
     */
    public int updateSysAgent(SysAgent sysAgent);

    /**
     * 批量删除代理管理
     *
     * @param agentIds 需要删除的代理管理主键集合
     * @return 结果
     */
    public int deleteSysAgentByAgentIds(Long[] agentIds);

    /**
     * 删除代理管理信息
     *
     * @param agentId 代理管理主键
     * @return 结果
     */
    public int deleteSysAgentByAgentId(Long agentId);

    /**
     * 获取非代理用户
     *
     * @return
     */
    public List<AgentInfoVo> getNonAgents(String username);

    /**
     * 获取代理的所有子代理，包括非子集代理
     *
     * @return
     */
    public List<Long> getSubAgents(Long agentId);

    /**
     * 检查代理
     *
     * @param agent
     * @param checkEnableAddSubagent 添加代理、添加授权和制卡操作用的是同一个函数，添加授权和制卡操作时不需要检查是否可发展下级代理
     */
    public void checkAgent(SysAgent agent, boolean checkEnableAddSubagent);

    /**
     * 获取代理
     * @param userId
     * @return
     */
    public Set<String> getAgentPermission(SysUser user);

}
