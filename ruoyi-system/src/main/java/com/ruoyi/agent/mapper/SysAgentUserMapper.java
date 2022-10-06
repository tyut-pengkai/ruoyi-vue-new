package com.ruoyi.agent.mapper;

import com.ruoyi.agent.domain.SysAgent;
import com.ruoyi.agent.domain.vo.AgentInfoVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 代理管理Mapper接口
 *
 * @author zwgu
 * @date 2022-06-11
 */
@Repository
public interface SysAgentUserMapper {
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
     * 删除代理管理
     *
     * @param agentId 代理管理主键
     * @return 结果
     */
    public int deleteSysAgentByAgentId(Long agentId);

    /**
     * 批量删除代理管理
     *
     * @param agentIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSysAgentByAgentIds(Long[] agentIds);

    /**
     * 获取非代理用户（同时去除管理员与作者）
     *
     * @return
     */
    @Select("SELECT DISTINCT\n" +
            "\tag.agent_id,\n" +
            "\tu.user_id,\n" +
            "\tu.user_name,\n" +
            "\tu.nick_name\n" +
            "FROM\n" +
            "\tsys_user u\n" +
            "\tLEFT JOIN sys_agent ag ON ag.user_id = u.user_id\n" +
            "\tLEFT JOIN sys_user_role ur ON ur.user_id = u.user_id\n" +
            "\tLEFT JOIN sys_role r ON r.role_id = ur.role_id \n" +
            "WHERE\n" +
            "\tu.del_flag = '0' \n" +
            "\tAND ag.user_id IS NULL \n" +
            "\tAND (\n" +
            "\tr.role_key IS NULL \n" +
            "\tOR r.role_key NOT IN ( 'sadmin', 'admin' )) AND ( user_name LIKE CONCAT('%',#{username},'%') OR nick_name LIKE CONCAT('%',#{username},'%') ) \n" +
            "\tLIMIT 10")
    public List<AgentInfoVo> getNonAgents(@Param("username") String username);

    /**
     * 获取代理的所有子代理
     *
     * @return
     */
    @Select("SELECT ag.agent_id FROM sys_agent ag WHERE ag.path LIKE CONCAT(( SELECT ag.path FROM sys_agent ag WHERE ag.agent_id = #{agentId} ),\n" +
            "\t'%' \n" +
            ") \n" +
            "AND ag.path != (\n" +
            "\tSELECT\n" +
            "\t\tag.path \n" +
            "\tFROM\n" +
            "\t\tsys_agent ag \n" +
            "\tWHERE\n" +
            "\tag.agent_id = #{agentId} \n" +
            "\t)")
    public List<Long> getSubAgents(@Param("agentId") Long agentId);
}
