package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OaWorkflowMapper;
import com.ruoyi.system.domain.OaWorkflow;
import com.ruoyi.system.service.IOaWorkflowService;

/**
 * 审批流程主Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@Service
public class OaWorkflowServiceImpl implements IOaWorkflowService 
{
    @Autowired
    private OaWorkflowMapper oaWorkflowMapper;

    /**
     * 查询审批流程主
     * 
     * @param id 审批流程主主键
     * @return 审批流程主
     */
    @Override
    public OaWorkflow selectOaWorkflowById(Long id)
    {
        return oaWorkflowMapper.selectOaWorkflowById(id);
    }

    /**
     * 查询审批流程主列表
     * 
     * @param oaWorkflow 审批流程主
     * @return 审批流程主
     */
    @Override
    public List<OaWorkflow> selectOaWorkflowList(OaWorkflow oaWorkflow)
    {
        return oaWorkflowMapper.selectOaWorkflowList(oaWorkflow);
    }

    /**
     * 新增审批流程主
     * 
     * @param oaWorkflow 审批流程主
     * @return 结果
     */
    @Override
    public int insertOaWorkflow(OaWorkflow oaWorkflow)
    {
        return oaWorkflowMapper.insertOaWorkflow(oaWorkflow);
    }

    /**
     * 修改审批流程主
     * 
     * @param oaWorkflow 审批流程主
     * @return 结果
     */
    @Override
    public int updateOaWorkflow(OaWorkflow oaWorkflow)
    {
        return oaWorkflowMapper.updateOaWorkflow(oaWorkflow);
    }

    /**
     * 批量删除审批流程主
     * 
     * @param ids 需要删除的审批流程主主键
     * @return 结果
     */
    @Override
    public int deleteOaWorkflowByIds(Long[] ids)
    {
        return oaWorkflowMapper.deleteOaWorkflowByIds(ids);
    }

    /**
     * 删除审批流程主信息
     * 
     * @param id 审批流程主主键
     * @return 结果
     */
    @Override
    public int deleteOaWorkflowById(Long id)
    {
        return oaWorkflowMapper.deleteOaWorkflowById(id);
    }
}
