package com.gaoyifeng.controller;

import com.gaoyifeng.common.infrastructure.Result;
import com.gaoyifeng.entity.FrpcConfig;
import com.gaoyifeng.entity.ProxyRule;
import com.gaoyifeng.service.ConfigService;
import com.gaoyifeng.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * frpc配置控制器
 */
@RestController
@RequestMapping("/api/configs")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @Autowired
    private WebSocketService webSocketService;

    /**
     * 获取所有配置
     */
    @GetMapping
    public Result<List<FrpcConfig>> getAllConfigs() {
        return Result.success(configService.getAllConfigs());
    }

    /**
     * 根据ID获取配置
     */
    @GetMapping("/{id}")
    public Result<FrpcConfig> getConfigById(@PathVariable Long id) {
        FrpcConfig config = configService.getConfigById(id);
        if (config == null) {
            return Result.fail("配置不存在");
        }
        return Result.success(config);
    }

    /**
     * 创建或更新配置
     */
    @PostMapping
    public Result<FrpcConfig> saveConfig(@RequestBody FrpcConfig config) {
        boolean isNew = config.getId() == null;
        FrpcConfig savedConfig = configService.saveConfig(config);

        // 发送WebSocket通知
        if (isNew) {
            webSocketService.sendNotification("success", "创建配置成功: " + config.getName());
            webSocketService.sendSystemEvent("config_created", savedConfig);
        } else {
            webSocketService.sendNotification("success", "更新配置成功: " + config.getName());
            webSocketService.sendSystemEvent("config_updated", savedConfig);
        }

        return Result.success(savedConfig);
    }

    /**
     * 删除配置
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteConfig(@PathVariable Long id) {
        FrpcConfig config = configService.getConfigById(id);
        if (config == null) {
            return Result.fail("配置不存在");
        }

        configService.deleteConfig(id);

        // 发送WebSocket通知
        webSocketService.sendNotification("info", "删除配置: " + config.getName());
        webSocketService.sendSystemEvent("config_deleted", id);

        return Result.success();
    }

    /**
     * 获取配置的代理规则
     */
    @GetMapping("/{configId}/rules")
    public Result<List<ProxyRule>> getRulesByConfigId(@PathVariable Long configId) {
        FrpcConfig config = configService.getConfigById(configId);
        if (config == null) {
            return Result.fail("配置不存在");
        }
        return Result.success(configService.getRulesByConfigId(configId));
    }

    /**
     * 创建或更新代理规则
     */
    @PostMapping("/rules")
    public Result<ProxyRule> saveRule(@RequestBody ProxyRule rule) {
        boolean isNew = rule.getId() == null;
        ProxyRule savedRule = configService.saveRule(rule);

        // 发送WebSocket通知
        if (isNew) {
            webSocketService.sendNotification("success", "创建代理规则成功: " + rule.getName());
            webSocketService.sendSystemEvent("rule_created", savedRule);
        } else {
            webSocketService.sendNotification("success", "更新代理规则成功: " + rule.getName());
            webSocketService.sendSystemEvent("rule_updated", savedRule);
        }

        return Result.success(savedRule);
    }

    /**
     * 删除代理规则
     */
    @DeleteMapping("/rules/{id}")
    public Result<Void> deleteRule(@PathVariable Long id) {
        ProxyRule rule = configService.getRuleById(id);
        if (rule == null) {
            return Result.fail("代理规则不存在");
        }

        configService.deleteRule(id);

        // 发送WebSocket通知
        webSocketService.sendNotification("info", "删除代理规则: " + rule.getName());
        webSocketService.sendSystemEvent("rule_deleted", id);

        return Result.success();
    }

    /**
     * 生成配置文件内容
     */
    @GetMapping("/{id}/content")
    public Result<String> generateConfigContent(@PathVariable Long id) {
        FrpcConfig config = configService.getConfigById(id);
        if (config == null) {
            return Result.fail("配置不存在");
        }

        String content = configService.generateConfigContent(id);
        return Result.success(content);
    }
}
