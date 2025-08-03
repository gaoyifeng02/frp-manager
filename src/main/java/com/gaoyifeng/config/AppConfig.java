package com.gaoyifeng.config;

import com.gaoyifeng.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.Resource;

/**
 * 应用程序配置类
 */
@Configuration
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

    @Resource
    private ProcessService processService;

    /**
     * 应用程序启动时执行
     */
    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationStart() {
        logger.info("应用程序启动，开始自动启动frpc进程");
        try {
            // 启动所有设置为自动启动的frpc配置
            processService.startAutoStartConfigs();
        } catch (Exception e) {
            logger.error("自动启动frpc进程失败", e);
        }
    }

    /**
     * 应用程序关闭时执行
     */
    @EventListener(ContextClosedEvent.class)
    public void onApplicationStop() {
        logger.info("应用程序关闭，停止所有frpc进程");
        try {
            // 停止所有运行中的frpc进程
            processService.stopAllProcesses();
        } catch (Exception e) {
            logger.error("停止frpc进程失败", e);
        }
    }
}
