package com.gaoyifeng.service;

import com.gaoyifeng.entity.FrpcConfig;
import com.gaoyifeng.entity.ProxyRule;
import com.gaoyifeng.mapper.ConfigMapper;
import com.gaoyifeng.mapper.RuleMapper;
import com.gaoyifeng.util.ConfigGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * frpc配置服务类
 */
@Service
public class ConfigService {

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private RuleMapper ruleMapper;

    /**
     * 获取所有frpc配置
     */
    public List<FrpcConfig> getAllConfigs() {
        return configMapper.selectAllConfigs();
    }

    /**
     * 根据ID获取frpc配置
     */
    public FrpcConfig getConfigById(Long id) {
        FrpcConfig config = configMapper.selectConfigById(id);
        if (config != null) {
            // 加载配置的代理规则
            config.setProxyRules(ruleMapper.selectRulesByConfigId(id));
        }
        return config;
    }

    /**
     * 保存frpc配置（新增或更新）
     */
    @Transactional
    public FrpcConfig saveConfig(FrpcConfig config) {
        if (config.getId() == null) {
            // 新增配置
            configMapper.insertConfig(config);
        } else {
            // 更新配置
            configMapper.updateConfig(config);
        }
        return config;
    }

    /**
     * 删除frpc配置
     */
    @Transactional
    public void deleteConfig(Long id) {
        // 先删除配置下的所有规则
        ruleMapper.deleteRulesByConfigId(id);
        // 再删除配置
        configMapper.deleteConfig(id);
    }

    /**
     * 获取配置的所有代理规则
     */
    public List<ProxyRule> getRulesByConfigId(Long configId) {
        return ruleMapper.selectRulesByConfigId(configId);
    }

    /**
     * 保存代理规则（新增或更新）
     */
    @Transactional
    public ProxyRule saveRule(ProxyRule rule) {
        if (rule.getId() == null) {
            // 新增规则
            ruleMapper.insertRule(rule);
        } else {
            // 更新规则
            ruleMapper.updateRule(rule);
        }
        return rule;
    }

    /**
     * 根据ID获取代理规则
     */
    public ProxyRule getRuleById(Long id) {
        return ruleMapper.selectRuleById(id);
    }

    /**
     * 删除代理规则
     */
    public void deleteRule(Long ruleId) {
        ruleMapper.deleteRule(ruleId);
    }

    /**
     * 生成frpc配置文件内容
     */
    public String generateConfigContent(Long configId) {
        FrpcConfig config = getConfigById(configId);
        if (config == null) {
            throw new RuntimeException("配置不存在");
        }
        return ConfigGenerator.generateIniContent(config);
    }

    /**
     * 获取自动启动的配置列表
     */
    public List<FrpcConfig> getAutoStartConfigs() {
        List<FrpcConfig> configs = configMapper.selectAutoStartConfigs();
        // 加载每个配置的代理规则
        for (FrpcConfig config : configs) {
            config.setProxyRules(ruleMapper.selectRulesByConfigId(config.getId()));
        }
        return configs;
    }
}
