package com.ruoyi.payment.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import com.ruoyi.payment.mapper.WebhookEventMapper;
import com.ruoyi.payment.domain.WebhookEvent;
import com.ruoyi.payment.service.IWebhookEventService;

/**
 * Webhook事件记录 服务层实现
 * 
 * @author ruoyi
 * @date 2025-06-19
 */
@Service
public class WebhookEventServiceImpl implements IWebhookEventService 
{
    @Autowired
    private WebhookEventMapper webhookEventMapper;

    /**
     * 检查事件是否已被处理
     * 尝试插入事件ID，如果成功，说明是新事件，返回false。
     * 如果插入时抛出DuplicateKeyException，说明事件已存在，返回true。
     *
     * @param eventId 事件ID
     * @return true 如果已处理, false 如果未处理
     */
    @Override
    public boolean isEventProcessed(String eventId) {
        return webhookEventMapper.selectWebhookEventByEventId(eventId) != null;
    }

    @Override
    public boolean recordEventProcessed(String eventId, String eventChannel, String eventType, String summary) {
        try {
            WebhookEvent event = new WebhookEvent();
            event.setEventId(eventId);
            event.setEventChannel(eventChannel);
            event.setEventType(eventType);
            event.setSummary(summary);
            webhookEventMapper.insertWebhookEvent(event);
            return true; // 插入成功
        } catch (DuplicateKeyException e) {
            return false; // 捕获到重复键异常
        }
    }
} 