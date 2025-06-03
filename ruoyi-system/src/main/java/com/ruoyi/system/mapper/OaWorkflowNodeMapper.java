package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.OaWorkflowNode;

/**
 * 流程审批节点Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
public interface OaWorkflowNodeMapper 
{
    /**
     * 查询流程审批节点
     * 
     * @param id 流程审批节点主键
     * @return 流程审批节点
     */
    public OaWorkflowNode selectOaWorkflowNodeById(Long id);

    /**
     * 查询流程审批节点列表
     * 
     * @param oaWorkflowNode 流程审批节点
     * @return 流程审批节点集合
     */
    public List<OaWorkflowNode> selectOaWorkflowNodeList(OaWorkflowNode oaWorkflowNode);

    /**
     * 新增流程审批节点
     * 
     * @param oaWorkflowNode 流程审批节点
     * @return 结果
     */
    public int insertOaWorkflowNode(OaWorkflowNode oaWorkflowNode);

    /**
     * 修改流程审批节点
     * 
     * @param oaWorkflowNode 流程审批节点
     * @return 结果
     */
    public int updateOaWorkflowNode(OaWorkflowNode oaWorkflowNode);

    /**
     * 删除流程审批节点
     * 
     * @param id 流程审批节点主键
     * @return 结果
     */
    public int deleteOaWorkflowNodeById(Long id);

    /**
     * 批量删除流程审批节点
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOaWorkflowNodeByIds(Long[] ids);
}
