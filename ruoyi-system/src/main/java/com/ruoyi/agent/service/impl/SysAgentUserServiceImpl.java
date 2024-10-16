package com.ruoyi.agent.service.impl;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.domain.vo.AgentInfoVo;
import com.ruoyi.agent.mapper.SysAgentUserMapper;
import com.ruoyi.agent.service.ISysAgentUserService;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 代理管理Service业务层处理
 *
 * @author zwgu
 * @date 2022-06-11
 */
@Service
public class SysAgentUserServiceImpl implements ISysAgentUserService {

    private static final Set<String> agentPerms;

    static {
        Field[] fields = SysAgent.class.getDeclaredFields();
        agentPerms = Arrays.stream(fields).map(Field::getName).filter(name -> name.startsWith("enable")).collect(Collectors.toSet());
    }

    @Autowired
    private SysAgentUserMapper sysAgentMapper;

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
    public List<AgentInfoVo> getNonAgents(String username) {
        return sysAgentMapper.getNonAgents(username);
    }

    /**
     * 获取代理的所有子代理
     *
     * @return
     */
    public List<Long> getSubAgents(Long agentId) {
        return sysAgentMapper.getSubAgents(agentId);
    }

    /**
     * 检查代理
     *
     * @param agent
     * @param checkEnableAddSubagent 添加代理、添加授权和制卡操作用的是同一个函数，添加授权和制卡操作时不需要检查是否可发展下级代理
     */
    public void checkAgent(SysAgent agent, boolean checkEnableAddSubagent) {
        if (agent != null) {
            if (!UserConstants.NORMAL.equals(agent.getStatus())) {
                throw new ServiceException("您的代理权限被冻结");
            }
            if (agent.getExpireTime() != null && !agent.getExpireTime().after(DateUtils.getNowDate())) {
                throw new ServiceException("您的代理权限已过期");
            }
            if (checkEnableAddSubagent) {
                if (!UserConstants.YES.equals(agent.getEnableAddSubagent())) {
                    throw new ServiceException("您没有发展下级代理的权限");
                }
            }
        } else {
            throw new ServiceException("代理信息缺失", 400);
        }

    }

    /**
     * 获取代理
     *
     * @param user
     * @return
     */
    @Override
    public Set<String> getAgentPermission(SysUser user) {
        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (user.isAdmin())
        {
            perms.add("*:*:*");
        }
        else {
            SysAgent agent = selectSysAgentByUserId(user.getUserId());
            if(agent != null) {
                for (String agentPerm : agentPerms) {
                    try {
                        Method method = SysAgent.class.getDeclaredMethod("get" + StringUtils.capitalize(agentPerm));
                        String invoke = method.invoke(agent).toString();
                        if(Objects.equals(UserConstants.YES, invoke)) {
                            perms.add(agentPerm);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return perms;
    }
}
