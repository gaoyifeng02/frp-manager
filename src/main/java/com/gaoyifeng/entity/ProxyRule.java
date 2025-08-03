package com.gaoyifeng.entity;

import java.util.Date;

/**
 * 代理规则实体类
 */
public class ProxyRule {

    private Long id;
    private Long configId;          // 关联的配置ID
    private String name;            // 规则名称
    private String type;            // 代理类型：tcp, udp, http, https, stcp, xtcp
    private String localIp;         // 本地IP
    private Integer localPort;      // 本地端口
    private Integer remotePort;     // 远程端口
    private String customDomains;   // 自定义域名，用于http/https类型
    private String subdomain;       // 子域名，用于http/https类型
    private Boolean useEncryption;  // 是否使用加密
    private Boolean useCompression; // 是否使用压缩
    private Date createTime;        // 创建时间
    private Date updateTime;        // 更新时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConfigId() {
        return configId;
    }

    public void setConfigId(Long configId) {
        this.configId = configId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public Integer getLocalPort() {
        return localPort;
    }

    public void setLocalPort(Integer localPort) {
        this.localPort = localPort;
    }

    public Integer getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
    }

    public String getCustomDomains() {
        return customDomains;
    }

    public void setCustomDomains(String customDomains) {
        this.customDomains = customDomains;
    }

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public Boolean getUseEncryption() {
        return useEncryption;
    }

    public void setUseEncryption(Boolean useEncryption) {
        this.useEncryption = useEncryption;
    }

    public Boolean getUseCompression() {
        return useCompression;
    }

    public void setUseCompression(Boolean useCompression) {
        this.useCompression = useCompression;
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

    @Override
    public String toString() {
        return "ProxyRule{" +
                "id=" + id +
                ", configId=" + configId +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", localIp='" + localIp + '\'' +
                ", localPort=" + localPort +
                ", remotePort=" + remotePort +
                ", customDomains='" + customDomains + '\'' +
                ", subdomain='" + subdomain + '\'' +
                ", useEncryption=" + useEncryption +
                ", useCompression=" + useCompression +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
