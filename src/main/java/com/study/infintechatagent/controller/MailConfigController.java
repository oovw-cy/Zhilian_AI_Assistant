package com.study.infintechatagent.controller;


import com.study.infintechatagent.cache.UserMailConfigCache;
import com.study.infintechatagent.model.dto.UserMailConfig;
import dev.langchain4j.service.MemoryId;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailConfigController {

    @Resource
    private UserMailConfigCache mailConfigCache;

    /**
     * 前端调用：保存用户的邮箱配置（SMTP四件套）
     * @param memoryId 用户唯一标识（必须传）
     * @param config 前端填写的4个配置
     */
    @PostMapping("/save-config")
    public String saveMailConfig(@MemoryId String memoryId, @RequestBody UserMailConfig config) {
        mailConfigCache.saveConfig(memoryId, config);
        return "✅ 邮箱配置保存成功！后续可直接发送邮件";
    }
}