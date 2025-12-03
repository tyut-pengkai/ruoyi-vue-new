package com.ruoyi.common.utils;

import java.io.File;
import java.util.List;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MailUtils {

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    /** 纯文本 */
    public void sendText(String to, String subject, String content) {
        send(to, subject, content, false, null);
    }

    /** HTML 单发 */
    public void sendHtml(String to, String subject, String html) {
        send(to, subject, html, true, null);
    }

    /** HTML 群发（抄送模式，性能足够） */
    public void sendHtmlBatch(List<String> toList, String subject, String html) {
        if (toList == null || toList.isEmpty()) return;
        String[] toArray = toList.toArray(new String[0]);
        send(String.join(",", toArray), subject, html, true, null);
    }

    /** 带附件 */
    public void sendWithFile(String to, String subject, String html, File file) {
        send(to, subject, html, true, file);
    }

    /** 真正发送 */
    private void send(String to, String subject, String text, boolean html, File file) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to.split(","));
            helper.setSubject(subject);
            helper.setText(text, html);
            if (file != null) helper.addAttachment(file.getName(), file);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("邮件发送失败", e);
        }
    }
}