package com.study.infintechatagent.cache;

import com.study.infintechatagent.model.dto.UserMailConfig;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存每个用户的邮箱配置（key=memoryId，value=邮箱配置）
 */
@Component
public class UserMailConfigCache {
    // 线程安全的Map，存储用户邮箱配置
    private final Map<String, UserMailConfig> configMap = new ConcurrentHashMap<>();

    // 保存用户配置
    public void saveConfig(String userId, UserMailConfig config) {
        configMap.put(userId, config);
    }

    // 获取用户配置
    public UserMailConfig getConfig(String userId) {
        return configMap.get(userId);
    }

    // 判断用户是否配置了邮箱
    public boolean hasConfig(String userId) {
        return configMap.containsKey(userId);
    }
}
