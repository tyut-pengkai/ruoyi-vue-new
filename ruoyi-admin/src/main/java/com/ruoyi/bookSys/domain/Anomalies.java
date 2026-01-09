package com.ruoyi.bookSys.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 异常记录对象 anomalies
 * 
 * @author ruoyi
 * @date 2025-12-31
 */
public class Anomalies extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 异常ID */
    private Long anomalyId;

    /** 相关记录ID */
    @Excel(name = "相关记录ID")
    private Long recordId;

    /** 异常类型(1:超时未离/2:黑名单预警/3:通行异常/4:体温异常) */
    @Excel(name = "异常类型(1:超时未离/2:黑名单预警/3:通行异常/4:体温异常)")
    private Integer type;

    /** 异常描述 */
    @Excel(name = "异常描述")
    private String description;

    /** 处理人 */
    @Excel(name = "处理人")
    private Long handledBy;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "处理时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date handledTime;

    /** 处理状态(1:待处理/2:已处理/3:误报) */
    @Excel(name = "处理状态(1:待处理/2:已处理/3:误报)")
    private Integer status;

    /** 严重程度(1:低/2:中/3:高) */
    @Excel(name = "严重程度(1:低/2:中/3:高)")
    private Integer severity;

    public void setAnomalyId(Long anomalyId) 
    {
        this.anomalyId = anomalyId;
    }

    public Long getAnomalyId() 
    {
        return anomalyId;
    }

    public void setRecordId(Long recordId) 
    {
        this.recordId = recordId;
    }

    public Long getRecordId() 
    {
        return recordId;
    }

    public void setType(Integer type) 
    {
        this.type = type;
    }

    public Integer getType() 
    {
        return type;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public void setHandledBy(Long handledBy) 
    {
        this.handledBy = handledBy;
    }

    public Long getHandledBy() 
    {
        return handledBy;
    }

    public void setHandledTime(Date handledTime) 
    {
        this.handledTime = handledTime;
    }

    public Date getHandledTime() 
    {
        return handledTime;
    }

    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }

    public void setSeverity(Integer severity) 
    {
        this.severity = severity;
    }

    public Integer getSeverity() 
    {
        return severity;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("anomalyId", getAnomalyId())
            .append("recordId", getRecordId())
            .append("type", getType())
            .append("description", getDescription())
            .append("handledBy", getHandledBy())
            .append("handledTime", getHandledTime())
            .append("status", getStatus())
            .append("severity", getSeverity())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
