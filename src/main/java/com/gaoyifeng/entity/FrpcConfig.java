package com.gaoyifeng.entity;

import java.util.Date;
import java.util.List;

/**
 * frpc配置实体类
 */
public class FrpcConfig {

    private Long id;
    private String name;                // 配置名称
    private String serverAddress;       // 服务器地址
    private Integer serverPort;         // 服务器端口
    private String token;               // 认证令牌
    private String frpcPath;            // frpc可执行文件路径
    private String logFile;             // 日志文件路径
    private Boolean enableLog;          // 是否启用日志
    private Integer logLevel;           // 日志级别：0-trace, 1-debug, 2-info, 3-warn, 4-error
    private Boolean autoStart;          // 是否自动启动
    private Date createTime;            // 创建时间
    private Date updateTime;            // 更新时间
    private List<ProxyRule> proxyRules; // 代理规则列表

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFrpcPath() {
        return frpcPath;
    }

    public void setFrpcPath(String frpcPath) {
        this.frpcPath = frpcPath;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public Boolean getEnableLog() {
        return enableLog;
    }

    public void setEnableLog(Boolean enableLog) {
        this.enableLog = enableLog;
    }

    public Integer getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(Integer logLevel) {
        this.logLevel = logLevel;
    }

    public Boolean getAutoStart() {
        return autoStart;
    }

    public void setAutoStart(Boolean autoStart) {
        this.autoStart = autoStart;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<ProxyRule> getProxyRules() {
        return proxyRules;
    }

    public void setProxyRules(List<ProxyRule> proxyRules) {
        this.proxyRules = proxyRules;
    }

    @Override
    public String toString() {
        return "FrpcConfig{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", serverAddress='" + serverAddress + '\'' +
                ", serverPort=" + serverPort +
                ", token='" + token + '\'' +
                ", frpcPath='" + frpcPath + '\'' +
                ", logFile='" + logFile + '\'' +
                ", enableLog=" + enableLog +
                ", logLevel=" + logLevel +
                ", autoStart=" + autoStart +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
