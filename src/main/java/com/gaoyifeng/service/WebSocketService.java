package com.gaoyifeng.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket服务类，用于向客户端推送消息
 */
@Service
public class WebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 向指定主题发送消息
     *
     * @param topic   主题
     * @param message 消息内容
     */
    public void sendMessage(String topic, Object message) {
        try {
            messagingTemplate.convertAndSend(topic, message);
            logger.debug("发送WebSocket消息到主题: {}", topic);
        } catch (Exception e) {
            logger.error("发送WebSocket消息失败", e);
        }
    }

    /**
     * 向指定配置的日志主题发送日志消息
     *
     * @param configId 配置ID
     * @param log      日志内容
     */
    public void sendLogMessage(Long configId, String log) {
        sendMessage("/topic/logs/" + configId, log);
    }

    /**
     * 向指定配置的状态主题发送状态更新
     *
     * @param configId 配置ID
     * @param status   状态信息
     */
    public void sendStatusUpdate(Long configId, Map<String, Object> status) {
        sendMessage("/topic/status/" + configId, status);
    }

    /**
     * 发送全局通知
     *
     * @param type    通知类型（info, warning, error, success）
     * @param message 通知内容
     */
    public void sendNotification(String type, String message) {
        Map<String, String> notification = new HashMap<>();
        notification.put("type", type);
        notification.put("message", message);
        notification.put("timestamp", String.valueOf(System.currentTimeMillis()));

        sendMessage("/topic/notifications", notification);
    }

    /**
     * 发送系统事件
     *
     * @param event   事件名称
     * @param data    事件数据
     */
    public void sendSystemEvent(String event, Object data) {
        Map<String, Object> eventMessage = new HashMap<>();
        eventMessage.put("event", event);
        eventMessage.put("data", data);
        eventMessage.put("timestamp", System.currentTimeMillis());

        sendMessage("/topic/system", eventMessage);
    }
}