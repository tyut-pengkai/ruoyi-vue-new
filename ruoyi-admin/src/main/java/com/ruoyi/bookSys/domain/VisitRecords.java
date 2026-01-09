package com.ruoyi.bookSys.domain;

import java.util.List;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 访客记录对象 visit_records
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
public class VisitRecords extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long recordId;

    /** 访客ID */
    @Excel(name = "访客ID")
    private Long visitorId;

    /** 被访住户ID */
    @Excel(name = "被访住户ID")
    private Long residentId;

    /** 来访事由 */
    @Excel(name = "来访事由")
    private String visitPurpose;

    /** 预计到达时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "预计到达时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date expectedArrival;

    /** 实际到达时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际到达时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualArrival;

    /** 实际离开时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际离开时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualLeave;

    /** 状态(1:预约中/2:已到达/3:已离开/4:已取消) */
    @Excel(name = "状态(1:预约中/2:已到达/3:已离开/4:已取消)")
    private Integer status;

    /** 审核状态(1:待审核/2:已同意/3:已拒绝) */
    @Excel(name = "审核状态(1:待审核/2:已同意/3:已拒绝)")
    private Integer approvalStatus;

    /** 审核人ID */
    @Excel(name = "审核人ID")
    private Long approverId;

    /** 拒绝原因 */
    @Excel(name = "拒绝原因")
    private String rejectReason;

    /** 访客通行码 */
    @Excel(name = "访客通行码")
    private String accessCode;

    /** 通行码有效期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "通行码有效期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date codeExpiry;

    /** 随行人数 */
    @Excel(name = "随行人数")
    private Long accompanyCount;

    /** 备注 */
    @Excel(name = "备注")
    private String notes;

    /** 通行记录信息 */
    private List<AccessLogs> accessLogsList;

    public void setRecordId(Long recordId) 
    {
        this.recordId = recordId;
    }

    public Long getRecordId() 
    {
        return recordId;
    }

    public void setVisitorId(Long visitorId) 
    {
        this.visitorId = visitorId;
    }

    public Long getVisitorId() 
    {
        return visitorId;
    }

    public void setResidentId(Long residentId) 
    {
        this.residentId = residentId;
    }

    public Long getResidentId() 
    {
        return residentId;
    }

    public void setVisitPurpose(String visitPurpose) 
    {
        this.visitPurpose = visitPurpose;
    }

    public String getVisitPurpose() 
    {
        return visitPurpose;
    }

    public void setExpectedArrival(Date expectedArrival) 
    {
        this.expectedArrival = expectedArrival;
    }

    public Date getExpectedArrival() 
    {
        return expectedArrival;
    }

    public void setActualArrival(Date actualArrival) 
    {
        this.actualArrival = actualArrival;
    }

    public Date getActualArrival() 
    {
        return actualArrival;
    }

    public void setActualLeave(Date actualLeave) 
    {
        this.actualLeave = actualLeave;
    }

    public Date getActualLeave() 
    {
        return actualLeave;
    }

    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    public void setApprovalStatus(Integer approvalStatus) 
    {
        this.approvalStatus = approvalStatus;
    }

    public Integer getApprovalStatus() 
    {
        return approvalStatus;
    }

    public void setApproverId(Long approverId) 
    {
        this.approverId = approverId;
    }

    public Long getApproverId() 
    {
        return approverId;
    }

    public void setRejectReason(String rejectReason) 
    {
        this.rejectReason = rejectReason;
    }

    public String getRejectReason() 
    {
        return rejectReason;
    }

    public void setAccessCode(String accessCode) 
    {
        this.accessCode = accessCode;
    }

    public String getAccessCode() 
    {
        return accessCode;
    }

    public void setCodeExpiry(Date codeExpiry) 
    {
        this.codeExpiry = codeExpiry;
    }

    public Date getCodeExpiry() 
    {
        return codeExpiry;
    }

    public void setAccompanyCount(Long accompanyCount) 
    {
        this.accompanyCount = accompanyCount;
    }

    public Long getAccompanyCount() 
    {
        return accompanyCount;
    }

    public void setNotes(String notes) 
    {
        this.notes = notes;
    }

    public String getNotes() 
    {
        return notes;
    }

    public List<AccessLogs> getAccessLogsList()
    {
        return accessLogsList;
    }

    public void setAccessLogsList(List<AccessLogs> accessLogsList)
    {
        this.accessLogsList = accessLogsList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("recordId", getRecordId())
            .append("visitorId", getVisitorId())
            .append("residentId", getResidentId())
            .append("visitPurpose", getVisitPurpose())
            .append("expectedArrival", getExpectedArrival())
            .append("actualArrival", getActualArrival())
            .append("actualLeave", getActualLeave())
            .append("status", getStatus())
            .append("approvalStatus", getApprovalStatus())
            .append("approverId", getApproverId())
            .append("rejectReason", getRejectReason())
            .append("accessCode", getAccessCode())
            .append("codeExpiry", getCodeExpiry())
            .append("accompanyCount", getAccompanyCount())
            .append("notes", getNotes())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("accessLogsList", getAccessLogsList())
            .toString();
    }
}
