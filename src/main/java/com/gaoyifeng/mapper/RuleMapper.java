package com.gaoyifeng.mapper;

import com.gaoyifeng.entity.ProxyRule;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 代理规则Mapper接口
 */
@Mapper
public interface RuleMapper {

    /**
     * 查询所有规则
     */
    @Select("SELECT * FROM proxy_rule")
    List<ProxyRule> selectAllRules();

    /**
     * 根据ID查询规则
     */
    @Select("SELECT * FROM proxy_rule WHERE id = #{id}")
    ProxyRule selectRuleById(Long id);

    /**
     * 根据配置ID查询规则
     */
    @Select("SELECT * FROM proxy_rule WHERE config_id = #{configId}")
    List<ProxyRule> selectRulesByConfigId(Long configId);

    /**
     * 插入规则
     */
    @Insert("INSERT INTO proxy_rule (config_id, name, type, local_ip, local_port, remote_port, " +
            "custom_domains, subdomain, use_encryption, use_compression, create_time, update_time) " +
            "VALUES (#{configId}, #{name}, #{type}, #{localIp}, #{localPort}, #{remotePort}, " +
            "#{customDomains}, #{subdomain}, #{useEncryption}, #{useCompression}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertRule(ProxyRule rule);

    /**
     * 更新规则
     */
    @Update("UPDATE proxy_rule SET config_id = #{configId}, name = #{name}, type = #{type}, " +
            "local_ip = #{localIp}, local_port = #{localPort}, remote_port = #{remotePort}, " +
            "custom_domains = #{customDomains}, subdomain = #{subdomain}, " +
            "use_encryption = #{useEncryption}, use_compression = #{useCompression}, " +
            "update_time = NOW() WHERE id = #{id}")
    int updateRule(ProxyRule rule);

    /**
     * 删除规则
     */
    @Delete("DELETE FROM proxy_rule WHERE id = #{id}")
    int deleteRule(Long id);

    /**
     * 删除配置下的所有规则
     */
    @Delete("DELETE FROM proxy_rule WHERE config_id = #{configId}")
    int deleteRulesByConfigId(Long configId);
}
