package com.ruoyi.payment.service;

/**
 * Webhook事件记录 服务层
 * 
 * @author ruoyi
 */
public interface IWebhookEventService 
{
    /**
     * 检查事件是否已被处理（仅查询）
     * @param eventId 事件ID
     * @return true 如果已处理, false 如果未处理
     */
    public boolean isEventProcessed(String eventId);

    /**
     * 记录已处理的事件
     * @param eventId 事件ID
     * @param eventChannel 事件渠道
     * @param eventType 事件类型
     * @param summary 事件概要
     * @return true 如果记录成功, false 如果记录失败（例如，因为已存在）
     */
    public boolean recordEventProcessed(String eventId, String eventChannel, String eventType, String summary);
} 