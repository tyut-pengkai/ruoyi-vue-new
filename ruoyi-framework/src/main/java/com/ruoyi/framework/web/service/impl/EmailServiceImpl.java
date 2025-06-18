package com.ruoyi.framework.web.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * 邮件发送服务实现
 * 
 * @author ruoyi
 */
@Service
public class EmailServiceImpl
{
    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private MailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    /**
     * 发送邮件
     * 
     * @param to 收件人
     * @param subject 主题
     * @param content 内容
     * @return 结果
     */
    public boolean sendEmail(String to, String subject, String content)
    {
        try
        {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);  // 设置发件人
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            
            mailSender.send(message);
            
            log.info("邮件发送成功 - 收件人: {}, 主题: {}", to, subject);
            return true;
        }
        catch (Exception e)
        {
            log.error("邮件发送失败 - 收件人: {}, 错误: {}", to, e.getMessage(), e);
            return false;
        }
    }
} 