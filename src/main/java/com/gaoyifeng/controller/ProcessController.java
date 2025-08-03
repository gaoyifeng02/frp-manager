package com.gaoyifeng.controller;

import com.gaoyifeng.common.infrastructure.Result;
import com.gaoyifeng.entity.FrpcConfig;
import com.gaoyifeng.service.ConfigService;
import com.gaoyifeng.service.ProcessService;
import com.gaoyifeng.service.WebSocketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * frpc进程控制器
 */
@RestController
@RequestMapping("/api/process")
public class ProcessController {

    @Resource
    private ProcessService processService;

    @Resource
    private ConfigService configService;

    @Resource
    private WebSocketService webSocketService;

    /**
     * 启动frpc进程
     */
    @PostMapping("/start/{configId}")
    public Result<Map<String, Object>> startProcess(@PathVariable Long configId) {
        FrpcConfig config = configService.getConfigById(configId);
        if (config == null) {
            return Result.fail("配置不存在");
        }

        try {
            Map<String, Object> result = processService.startFrpcProcess(configId);

            // 发送WebSocket通知
            if ((boolean) result.get("success")) {
                webSocketService.sendNotification("success", "启动frpc进程成功: " + config.getName());
                webSocketService.sendSystemEvent("process_started", configId);
                return Result.success(result);
            } else {
                webSocketService.sendNotification("error", "启动frpc进程失败: " + config.getName());
                return Result.fail("启动frpc进程失败");
            }
        } catch (Exception e) {
            webSocketService.sendNotification("error", "启动frpc进程失败: " + config.getName() + " - " + e.getMessage());
            return Result.fail("启动frpc进程失败: " + e.getMessage());
        }
    }

    /**
     * 停止frpc进程
     */
    @PostMapping("/stop/{configId}")
    public Result<Map<String, Object>> stopProcess(@PathVariable Long configId) {
        FrpcConfig config = configService.getConfigById(configId);
        if (config == null) {
            return Result.fail("配置不存在");
        }

        try {
            Map<String, Object> result = processService.stopFrpcProcess(configId);

            // 发送WebSocket通知
            if ((boolean) result.get("success")) {
                webSocketService.sendNotification("info", "停止frpc进程: " + config.getName());
                webSocketService.sendSystemEvent("process_stopped", configId);
                return Result.success(result);
            } else {
                webSocketService.sendNotification("warning", "停止frpc进程失败: " + config.getName());
                return Result.fail("停止frpc进程失败");
            }
        } catch (Exception e) {
            webSocketService.sendNotification("error", "停止frpc进程失败: " + config.getName() + " - " + e.getMessage());
            return Result.fail("停止frpc进程失败: " + e.getMessage());
        }
    }

    /**
     * 重启frpc进程
     */
    @PostMapping("/restart/{configId}")
    public Result<Map<String, Object>> restartProcess(@PathVariable Long configId) {
        FrpcConfig config = configService.getConfigById(configId);
        if (config == null) {
            return Result.fail("配置不存在");
        }

        try {
            Map<String, Object> result = processService.restartFrpcProcess(configId);

            // 发送WebSocket通知
            if ((boolean) result.get("success")) {
                webSocketService.sendNotification("success", "重启frpc进程成功: " + config.getName());
                webSocketService.sendSystemEvent("process_restarted", configId);
                return Result.success(result);
            } else {
                webSocketService.sendNotification("error", "重启frpc进程失败: " + config.getName());
                return Result.fail("重启frpc进程失败");
            }
        } catch (Exception e) {
            webSocketService.sendNotification("error", "重启frpc进程失败: " + config.getName() + " - " + e.getMessage());
            return Result.fail("重启frpc进程失败: " + e.getMessage());
        }
    }

    /**
     * 获取frpc进程状态
     */
    @GetMapping("/status/{configId}")
    public Result<Map<String, Object>> getProcessStatus(@PathVariable Long configId) {
        FrpcConfig config = configService.getConfigById(configId);
        if (config == null) {
            return Result.fail("配置不存在");
        }

        Map<String, Object> status = processService.getFrpcProcessStatus(configId);
        return Result.success(status);
    }

    /**
     * 获取所有frpc进程状态
     */
    @GetMapping("/status")
    public Result<Map<Long, Map<String, Object>>> getAllProcessStatus() {
        Map<Long, Map<String, Object>> allStatus = processService.getAllFrpcProcessStatus();
        return Result.success(allStatus);
    }

    /**
     * 获取frpc进程日志
     */
    @GetMapping("/logs/{configId}")
    public Result<Map<String, Object>> getProcessLogs(
            @PathVariable Long configId,
            @RequestParam(defaultValue = "100") Integer lines) {

        FrpcConfig config = configService.getConfigById(configId);
        if (config == null) {
            return Result.fail("配置不存在");
        }

        String logs = processService.getFrpcProcessLogs(configId, lines);

        Map<String, Object> response = new HashMap<>();
        response.put("configId", configId);
        response.put("logs", logs);

        return Result.success(response);
    }
}
