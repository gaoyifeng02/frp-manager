package com.gaoyifeng.service;

import com.gaoyifeng.entity.FrpcConfig;
import com.gaoyifeng.util.ConfigGenerator;
import com.gaoyifeng.util.ProcessManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * frpc进程服务类
 */
@Service
public class ProcessService {

    private static final Logger logger = LoggerFactory.getLogger(ProcessService.class);

    @Autowired
    private ConfigService configService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // 存储配置ID与进程管理器的映射关系
    private final Map<Long, ProcessManager> processManagers = new ConcurrentHashMap<>();

    /**
     * 启动frpc进程
     */
    public Map<String, Object> startFrpcProcess(Long configId) {
        FrpcConfig config = configService.getConfigById(configId);
        if (config == null) {
            throw new RuntimeException("配置不存在");
        }

        // 检查frpc可执行文件是否存在
        File frpcFile = new File(config.getFrpcPath());
        if (!frpcFile.exists() || !frpcFile.canExecute()) {
            throw new RuntimeException("frpc可执行文件不存在或无执行权限: " + config.getFrpcPath());
        }

        // 生成临时配置文件
        String configContent = ConfigGenerator.generateIniContent(config);
        Path tempConfigPath;
        try {
            tempConfigPath = Files.createTempFile("frpc_" + configId + "_", ".ini");
            Files.write(tempConfigPath, configContent.getBytes());
        } catch (IOException e) {
            logger.error("创建临时配置文件失败", e);
            throw new RuntimeException("创建临时配置文件失败: " + e.getMessage());
        }

        // 构建启动命令
        String[] command = {
            config.getFrpcPath(),
            "-c",
            tempConfigPath.toString()
        };

        // 创建进程管理器
        ProcessManager processManager = new ProcessManager(command, config.getLogFile(),
                message -> messagingTemplate.convertAndSend("/topic/logs/" + configId, message));

        // 启动进程
        boolean success = processManager.start();

        if (success) {
            // 存储进程管理器
            processManagers.put(configId, processManager);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("message", "frpc进程启动成功");
            result.put("pid", processManager.getPid());
            result.put("configPath", tempConfigPath.toString());
            return result;
        } else {
            try {
                // 删除临时配置文件
                Files.deleteIfExists(tempConfigPath);
            } catch (IOException e) {
                logger.warn("删除临时配置文件失败", e);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "frpc进程启动失败");
            return result;
        }
    }

    /**
     * 停止frpc进程
     */
    public Map<String, Object> stopFrpcProcess(Long configId) {
        ProcessManager processManager = processManagers.get(configId);
        if (processManager == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "frpc进程未运行");
            return result;
        }

        boolean success = processManager.stop();
        processManagers.remove(configId);

        Map<String, Object> result = new HashMap<>();
        result.put("success", success);
        result.put("message", success ? "frpc进程已停止" : "停止frpc进程失败");
        return result;
    }

    /**
     * 重启frpc进程
     */
    public Map<String, Object> restartFrpcProcess(Long configId) {
        stopFrpcProcess(configId);
        return startFrpcProcess(configId);
    }

    /**
     * 获取frpc进程状态
     */
    public Map<String, Object> getFrpcProcessStatus(Long configId) {
        ProcessManager processManager = processManagers.get(configId);

        Map<String, Object> status = new HashMap<>();
        status.put("running", processManager != null && processManager.isRunning());

        if (processManager != null && processManager.isRunning()) {
            status.put("pid", processManager.getPid());
            status.put("startTime", processManager.getStartTime());
            status.put("uptime", processManager.getUptime());
        }

        return status;
    }

    /**
     * 获取所有frpc进程状态
     */
    public Map<Long, Map<String, Object>> getAllFrpcProcessStatus() {
        Map<Long, Map<String, Object>> allStatus = new HashMap<>();

        for (Map.Entry<Long, ProcessManager> entry : processManagers.entrySet()) {
            Long configId = entry.getKey();
            allStatus.put(configId, getFrpcProcessStatus(configId));
        }

        return allStatus;
    }

    /**
     * 获取frpc进程日志
     */
    public String getFrpcProcessLogs(Long configId, Integer lines) {
        FrpcConfig config = configService.getConfigById(configId);
        if (config == null || config.getLogFile() == null) {
            return "日志文件不存在";
        }

        Path logPath = Paths.get(config.getLogFile());
        if (!Files.exists(logPath)) {
            return "日志文件不存在: " + config.getLogFile();
        }

        try {
            List<String> logLines = Files.readAllLines(logPath);
            int startIndex = Math.max(0, logLines.size() - lines);
            return String.join("\n", logLines.subList(startIndex, logLines.size()));
        } catch (IOException e) {
            logger.error("读取日志文件失败", e);
            return "读取日志文件失败: " + e.getMessage();
        }
    }

    /**
     * 启动所有自动启动的配置
     */
    public void startAutoStartConfigs() {
        List<FrpcConfig> autoStartConfigs = configService.getAutoStartConfigs();
        for (FrpcConfig config : autoStartConfigs) {
            try {
                startFrpcProcess(config.getId());
                logger.info("自动启动frpc配置: {}", config.getName());
            } catch (Exception e) {
                logger.error("自动启动frpc配置失败: {}", config.getName(), e);
            }
        }
    }

    /**
     * 停止所有进程
     */
    public void stopAllProcesses() {
        for (Long configId : processManagers.keySet()) {
            try {
                stopFrpcProcess(configId);
            } catch (Exception e) {
                logger.error("停止frpc进程失败: {}", configId, e);
            }
        }
    }
}
