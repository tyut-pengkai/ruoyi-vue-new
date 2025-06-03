package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 流程审批节点对象 oa_workflow_node
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
public class OaWorkflowNode extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private Long id;

    /** 流程ID */
    @Excel(name = "流程ID")
    private Long workflowId;

    /** 审批人ID */
    @Excel(name = "审批人ID")
    private Long approverId;

    /** 审批状态(0:待处理/1:同意/2:拒绝) */
    @Excel(name = "审批状态(0:待处理/1:同意/2:拒绝)")
    private Integer approvalStatus;

    /** 审批意见 */
    @Excel(name = "审批意见")
    private String approvalComment;

    /** 审批时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审批时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date approvalTime;

    /** 节点顺序 */
    @Excel(name = "节点顺序")
    private Long nodeOrder;

    /** 更新人 */
    @Excel(name = "更新人")
    private Long updatedBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setWorkflowId(Long workflowId) 
    {
        this.workflowId = workflowId;
    }

    public Long getWorkflowId() 
    {
        return workflowId;
    }

    public void setApproverId(Long approverId) 
    {
        this.approverId = approverId;
    }

    public Long getApproverId() 
    {
        return approverId;
    }

    public void setApprovalStatus(Integer approvalStatus) 
    {
        this.approvalStatus = approvalStatus;
    }

    public Integer getApprovalStatus() 
    {
        return approvalStatus;
    }

    public void setApprovalComment(String approvalComment) 
    {
        this.approvalComment = approvalComment;
    }

    public String getApprovalComment() 
    {
        return approvalComment;
    }

    public void setApprovalTime(Date approvalTime) 
    {
        this.approvalTime = approvalTime;
    }

    public Date getApprovalTime() 
    {
        return approvalTime;
    }

    public void setNodeOrder(Long nodeOrder) 
    {
        this.nodeOrder = nodeOrder;
    }

    public Long getNodeOrder() 
    {
        return nodeOrder;
    }

    public void setUpdatedBy(Long updatedBy) 
    {
        this.updatedBy = updatedBy;
    }

    public Long getUpdatedBy() 
    {
        return updatedBy;
    }

    public void setUpdatedTime(Date updatedTime) 
    {
        this.updatedTime = updatedTime;
    }

    public Date getUpdatedTime() 
    {
        return updatedTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("workflowId", getWorkflowId())
            .append("approverId", getApproverId())
            .append("approvalStatus", getApprovalStatus())
            .append("approvalComment", getApprovalComment())
            .append("approvalTime", getApprovalTime())
            .append("nodeOrder", getNodeOrder())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
