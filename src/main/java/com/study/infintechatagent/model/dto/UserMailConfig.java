package com.study.infintechatagent.model.dto;

import lombok.Data;

/**
 * 用户的邮箱配置（SMTP四件套）
 */
@Data
public class UserMailConfig {
    private String smtpHost;    // SMTP服务器
    private Integer smtpPort;   // SMTP端口
    private String fromEmail;   // 发件人邮箱
    private String authCode;    // 授权码
}
