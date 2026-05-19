package com.study.infintechatagent.model.dto;

import lombok.Data;

//邮件发送人设置
@Data
public class MailConfigDTO {
    // SMTP服务器 例：smtp.qq.com / smtp.163.com
    private String smtpHost;
    // SMTP端口 例：587 / 465
    private Integer smtpPort;
    // 发件人账号（邮箱）
    private String username;
    // 授权码（不是登录密码）
    private String password;
    // 发件人显示邮箱（一般和username一致）
    private String fromEmail;
}
