package com.ruoyi.payment.mapper;

import com.ruoyi.payment.domain.WebhookEvent;

/**
 * Webhook事件记录 Mapper接口
 * 
 * @author ruoyi
 * @date 2025-06-19
 */
public interface WebhookEventMapper 
{
    /**
     * 根据EventId查询Webhook事件
     * 
     * @param eventId PayPal Webhook Event ID
     * @return WebhookEvent
     */
    public WebhookEvent selectWebhookEventByEventId(String eventId);

    /**
     * 新增WebhookEvent
     * 
     * @param webhookEvent WebhookEvent
     * @return 结果
     */
    public int insertWebhookEvent(WebhookEvent webhookEvent);
} 