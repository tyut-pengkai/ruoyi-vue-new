package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.OaWorkflow;

/**
 * 审批流程主Service接口
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
public interface IOaWorkflowService 
{
    /**
     * 查询审批流程主
     * 
     * @param id 审批流程主主键
     * @return 审批流程主
     */
    public OaWorkflow selectOaWorkflowById(Long id);

    /**
     * 查询审批流程主列表
     * 
     * @param oaWorkflow 审批流程主
     * @return 审批流程主集合
     */
    public List<OaWorkflow> selectOaWorkflowList(OaWorkflow oaWorkflow);

    /**
     * 新增审批流程主
     * 
     * @param oaWorkflow 审批流程主
     * @return 结果
     */
    public int insertOaWorkflow(OaWorkflow oaWorkflow);

    /**
     * 修改审批流程主
     * 
     * @param oaWorkflow 审批流程主
     * @return 结果
     */
    public int updateOaWorkflow(OaWorkflow oaWorkflow);

    /**
     * 批量删除审批流程主
     * 
     * @param ids 需要删除的审批流程主主键集合
     * @return 结果
     */
    public int deleteOaWorkflowByIds(Long[] ids);

    /**
     * 删除审批流程主信息
     * 
     * @param id 审批流程主主键
     * @return 结果
     */
    public int deleteOaWorkflowById(Long id);
}
