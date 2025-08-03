-- 创建frpc配置表
CREATE TABLE IF NOT EXISTS frpc_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    server_address VARCHAR(100) NOT NULL,
    server_port INT NOT NULL,
    token VARCHAR(100),
    enable_log BOOLEAN DEFAULT TRUE,
    log_file VARCHAR(255) DEFAULT 'frpc.log',
    log_level INT DEFAULT 2 COMMENT '0-trace,1-debug,2-info,3-warn,4-error',
    auto_start BOOLEAN DEFAULT FALSE,
    config_file_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建代理规则表
CREATE TABLE IF NOT EXISTS proxy_rule (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    config_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL COMMENT 'tcp,udp,http,https,stcp,xtcp',
    local_ip VARCHAR(100) NOT NULL,
    local_port INT NOT NULL,
    remote_port INT,
    custom_domains VARCHAR(255),
    subdomain VARCHAR(100),
    use_encryption BOOLEAN DEFAULT FALSE,
    use_compression BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (config_id) REFERENCES frpc_config(id) ON DELETE CASCADE,
    UNIQUE KEY uk_config_name (config_id, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建进程状态表
CREATE TABLE IF NOT EXISTS process_status (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    config_id BIGINT NOT NULL,
    pid BIGINT,
    status VARCHAR(20) DEFAULT 'STOPPED' COMMENT 'RUNNING,STOPPED,ERROR',
    start_time TIMESTAMP NULL,
    exit_time TIMESTAMP NULL,
    exit_code INT,
    last_error TEXT,
    FOREIGN KEY (config_id) REFERENCES frpc_config(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
