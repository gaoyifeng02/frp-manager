package com.gaoyifeng.mapper;

import com.gaoyifeng.entity.FrpcConfig;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * frpc配置Mapper接口
 */
@Mapper
public interface ConfigMapper {

    /**
     * 查询所有配置
     */
    @Select("SELECT * FROM frpc_config")
    List<FrpcConfig> selectAllConfigs();

    /**
     * 根据ID查询配置
     */
    @Select("SELECT * FROM frpc_config WHERE id = #{id}")
    FrpcConfig selectConfigById(Long id);

    /**
     * 插入配置
     */
    @Insert("INSERT INTO frpc_config (name, server_address, server_port, token, frpc_path, " +
            "log_file, enable_log, log_level, auto_start, create_time, update_time) " +
            "VALUES (#{name}, #{serverAddress}, #{serverPort}, #{token}, #{frpcPath}, " +
            "#{logFile}, #{enableLog}, #{logLevel}, #{autoStart}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertConfig(FrpcConfig config);

    /**
     * 更新配置
     */
    @Update("UPDATE frpc_config SET name = #{name}, server_address = #{serverAddress}, " +
            "server_port = #{serverPort}, token = #{token}, frpc_path = #{frpcPath}, " +
            "log_file = #{logFile}, enable_log = #{enableLog}, log_level = #{logLevel}, " +
            "auto_start = #{autoStart}, update_time = NOW() WHERE id = #{id}")
    int updateConfig(FrpcConfig config);

    /**
     * 删除配置
     */
    @Delete("DELETE FROM frpc_config WHERE id = #{id}")
    int deleteConfig(Long id);

    /**
     * 查询自动启动的配置
     */
    @Select("SELECT * FROM frpc_config WHERE auto_start = 1")
    List<FrpcConfig> selectAutoStartConfigs();
}
