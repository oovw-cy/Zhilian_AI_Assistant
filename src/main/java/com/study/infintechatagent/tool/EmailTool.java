package com.study.infintechatagent.tool;

import com.study.infintechatagent.cache.UserMailConfigCache;
import com.study.infintechatagent.model.dto.UserMailConfig;
import com.study.infintechatagent.monitor.MonitorContext;
import com.study.infintechatagent.monitor.MonitorContextHolder;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.service.MemoryId;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@Slf4j
public class EmailTool {

    @Resource
    private UserMailConfigCache configCache;

    /**
     * 发送邮件（用户已提前配置SMTP信息，无需重复输入）
     * targetEmail: 收件人邮箱
     * subject: 邮件标题
     * content: 邮件内容
     */
    @Tool("发送邮件给指定收件人，需要提供收件人邮箱、邮件标题、邮件内容，发件配置已提前保存")
    public String sendEmail(String targetEmail, String subject, String content) {
        MonitorContext context = MonitorContextHolder.getContext();
        try {
            // 1. 自动获取当前用户保存的邮箱配置
            String userId = String.valueOf(context.getUserId());
            UserMailConfig config = configCache.getConfig(userId);
            if (config == null) {
                return "❌ 请先在前端填写并保存SMTP邮箱配置，再发送邮件";
            }

            log.info("Tool调用: 发送邮件 -> 收件人:{}，标题:{}", targetEmail, subject);

            // 2. 动态创建邮件发送器（使用用户配置）
            JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
            mailSender.setHost(config.getSmtpHost());
            mailSender.setPort(config.getSmtpPort());
            mailSender.setUsername(config.getFromEmail());
            mailSender.setPassword(config.getAuthCode());

            // 邮箱通用配置（兼容QQ/163/126）
            Properties props = mailSender.getJavaMailProperties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", config.getSmtpHost());
            props.put("mail.transport.protocol", "smtp");

            // 3. 构建邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(config.getFromEmail());
            message.setTo(targetEmail);
            message.setSubject(subject);
            message.setText(content);

            // 4. 发送
            mailSender.send(message);


            return "✅ 邮件发送成功！收件人：" + targetEmail;

        } catch (Exception e) {
            log.error("邮件发送失败", e);
            return "❌ 邮件发送失败：" + e.getMessage();
        }
    }
}