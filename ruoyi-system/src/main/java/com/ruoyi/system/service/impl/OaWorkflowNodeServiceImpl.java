package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.OaWorkflowNodeMapper;
import com.ruoyi.system.domain.OaWorkflowNode;
import com.ruoyi.system.service.IOaWorkflowNodeService;

/**
 * 流程审批节点Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
@Service
public class OaWorkflowNodeServiceImpl implements IOaWorkflowNodeService 
{
    @Autowired
    private OaWorkflowNodeMapper oaWorkflowNodeMapper;

    /**
     * 查询流程审批节点
     * 
     * @param id 流程审批节点主键
     * @return 流程审批节点
     */
    @Override
    public OaWorkflowNode selectOaWorkflowNodeById(Long id)
    {
        return oaWorkflowNodeMapper.selectOaWorkflowNodeById(id);
    }

    /**
     * 查询流程审批节点列表
     * 
     * @param oaWorkflowNode 流程审批节点
     * @return 流程审批节点
     */
    @Override
    public List<OaWorkflowNode> selectOaWorkflowNodeList(OaWorkflowNode oaWorkflowNode)
    {
        return oaWorkflowNodeMapper.selectOaWorkflowNodeList(oaWorkflowNode);
    }

    /**
     * 新增流程审批节点
     * 
     * @param oaWorkflowNode 流程审批节点
     * @return 结果
     */
    @Override
    public int insertOaWorkflowNode(OaWorkflowNode oaWorkflowNode)
    {
        return oaWorkflowNodeMapper.insertOaWorkflowNode(oaWorkflowNode);
    }

    /**
     * 修改流程审批节点
     * 
     * @param oaWorkflowNode 流程审批节点
     * @return 结果
     */
    @Override
    public int updateOaWorkflowNode(OaWorkflowNode oaWorkflowNode)
    {
        return oaWorkflowNodeMapper.updateOaWorkflowNode(oaWorkflowNode);
    }

    /**
     * 批量删除流程审批节点
     * 
     * @param ids 需要删除的流程审批节点主键
     * @return 结果
     */
    @Override
    public int deleteOaWorkflowNodeByIds(Long[] ids)
    {
        return oaWorkflowNodeMapper.deleteOaWorkflowNodeByIds(ids);
    }

    /**
     * 删除流程审批节点信息
     * 
     * @param id 流程审批节点主键
     * @return 结果
     */
    @Override
    public int deleteOaWorkflowNodeById(Long id)
    {
        return oaWorkflowNodeMapper.deleteOaWorkflowNodeById(id);
    }
}
