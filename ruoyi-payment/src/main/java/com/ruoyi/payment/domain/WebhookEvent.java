package com.ruoyi.payment.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Webhook事件记录对象 webhook_events
 * 
 * @author ruoyi
 * @date 2025-06-19
 */
public class WebhookEvent extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 事件Id */
    private String eventId;

    /** 事件渠道(paypal等) */
    private String eventChannel;

    /** 事件类型 */
    private String eventType;

    /** 事件概要 */
    private String summary;

    public void setEventId(String eventId) 
    {
        this.eventId = eventId;
    }

    public String getEventId() 
    {
        return eventId;
    }
    public void setEventChannel(String eventChannel) 
    {
        this.eventChannel = eventChannel;
    }

    public String getEventChannel() 
    {
        return eventChannel;
    }
    public void setEventType(String eventType) 
    {
        this.eventType = eventType;
    }

    public String getEventType() 
    {
        return eventType;
    }
    public void setSummary(String summary) 
    {
        this.summary = summary;
    }

    public String getSummary() 
    {
        return summary;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("eventId", getEventId())
            .append("eventChannel", getEventChannel())
            .append("eventType", getEventType())
            .append("summary", getSummary())
            .append("createTime", getCreateTime())
            .toString();
    }
} 