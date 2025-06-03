package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 审批流程主对象 oa_workflow
 * 
 * @author ruoyi
 * @date 2025-06-03
 */
public class OaWorkflow extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 流程ID */
    private Long id;

    /** 流程类型(0:报销/1:开票/2:出款/3:盖章/4:资质/5:借资料) */
    @Excel(name = "流程类型(0:报销/1:开票/2:出款/3:盖章/4:资质/5:借资料)")
    private Integer processType;

    /** 申请人ID */
    @Excel(name = "申请人ID")
    private Long applicantId;

    /** 状态(0:审批中/1:通过/2:拒绝) */
    @Excel(name = "状态(0:审批中/1:通过/2:拒绝)")
    private Integer currentStatus;

    /** 审批内容(JSON格式) */
    @Excel(name = "审批内容(JSON格式)")
    private String content;

    /** 申请时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "申请时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdTime;

    /** 创建人 */
    @Excel(name = "创建人")
    private Long createdBy;

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

    public void setProcessType(Integer processType) 
    {
        this.processType = processType;
    }

    public Integer getProcessType() 
    {
        return processType;
    }

    public void setApplicantId(Long applicantId) 
    {
        this.applicantId = applicantId;
    }

    public Long getApplicantId() 
    {
        return applicantId;
    }

    public void setCurrentStatus(Integer currentStatus) 
    {
        this.currentStatus = currentStatus;
    }

    public Integer getCurrentStatus() 
    {
        return currentStatus;
    }

    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    public void setCreatedTime(Date createdTime) 
    {
        this.createdTime = createdTime;
    }

    public Date getCreatedTime() 
    {
        return createdTime;
    }

    public void setCreatedBy(Long createdBy) 
    {
        this.createdBy = createdBy;
    }

    public Long getCreatedBy() 
    {
        return createdBy;
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
            .append("processType", getProcessType())
            .append("applicantId", getApplicantId())
            .append("currentStatus", getCurrentStatus())
            .append("content", getContent())
            .append("createdTime", getCreatedTime())
            .append("createdBy", getCreatedBy())
            .append("updatedBy", getUpdatedBy())
            .append("updatedTime", getUpdatedTime())
            .toString();
    }
}
