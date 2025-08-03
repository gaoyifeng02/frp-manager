package com.gaoyifeng.util;

import com.gaoyifeng.entity.FrpcConfig;
import com.gaoyifeng.entity.ProxyRule;

import java.util.List;

/**
 * frpc配置文件生成工具类
 */
public class ConfigGenerator {

    /**
     * 生成frpc的ini配置文件内容
     *
     * @param config frpc配置对象
     * @return ini格式的配置文件内容
     */
    public static String generateIniContent(FrpcConfig config) {
        StringBuilder sb = new StringBuilder();

        // 生成通用配置部分
        sb.append("[common]\n");
        sb.append("server_addr = ").append(config.getServerAddress()).append("\n");
        sb.append("server_port = ").append(config.getServerPort()).append("\n");

        // 如果有token，添加token配置
        if (config.getToken() != null && !config.getToken().isEmpty()) {
            sb.append("token = ").append(config.getToken()).append("\n");
        }

        // 日志配置
        if (Boolean.TRUE.equals(config.getEnableLog())) {
            sb.append("log_file = ").append(config.getLogFile()).append("\n");

            // 日志级别：0-trace, 1-debug, 2-info, 3-warn, 4-error
            String logLevel;
            switch (config.getLogLevel()) {
                case 0:
                    logLevel = "trace";
                    break;
                case 1:
                    logLevel = "debug";
                    break;
                case 2:
                    logLevel = "info";
                    break;
                case 3:
                    logLevel = "warn";
                    break;
                case 4:
                    logLevel = "error";
                    break;
                default:
                    logLevel = "info";
            }
            sb.append("log_level = ").append(logLevel).append("\n");
        }

        sb.append("\n");

        // 生成代理规则部分
        List<ProxyRule> rules = config.getProxyRules();
        if (rules != null && !rules.isEmpty()) {
            for (ProxyRule rule : rules) {
                sb.append("[").append(rule.getName()).append("]\n");
                sb.append("type = ").append(rule.getType()).append("\n");
                sb.append("local_ip = ").append(rule.getLocalIp()).append("\n");
                sb.append("local_port = ").append(rule.getLocalPort()).append("\n");

                // 根据代理类型添加不同的配置
                switch (rule.getType().toLowerCase()) {
                    case "tcp":
                    case "udp":
                        if (rule.getRemotePort() != null) {
                            sb.append("remote_port = ").append(rule.getRemotePort()).append("\n");
                        }
                        break;
                    case "http":
                    case "https":
                        // 对于HTTP/HTTPS代理，添加自定义域名或子域名
                        if (rule.getCustomDomains() != null && !rule.getCustomDomains().isEmpty()) {
                            sb.append("custom_domains = ").append(rule.getCustomDomains()).append("\n");
                        }
                        if (rule.getSubdomain() != null && !rule.getSubdomain().isEmpty()) {
                            sb.append("subdomain = ").append(rule.getSubdomain()).append("\n");
                        }
                        break;
                    case "stcp":
                    case "xtcp":
                        // 对于STCP/XTCP代理，可能需要额外的配置
                        // 这里只是一个简单的示例，实际使用中可能需要更多配置
                        sb.append("role = server\n");
                        break;
                }

                // 添加加密和压缩配置
                if (Boolean.TRUE.equals(rule.getUseEncryption())) {
                    sb.append("use_encryption = true\n");
                }
                if (Boolean.TRUE.equals(rule.getUseCompression())) {
                    sb.append("use_compression = true\n");
                }

                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
