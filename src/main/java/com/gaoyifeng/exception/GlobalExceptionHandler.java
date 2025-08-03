package com.gaoyifeng.exception;

import com.gaoyifeng.common.infrastructure.Result;
import com.gaoyifeng.service.WebSocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private WebSocketService webSocketService;

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException ex, WebRequest request) {
        logger.error("业务异常: {}", ex.getMessage(), ex);

        // 发送WebSocket通知
        webSocketService.sendNotification("error", ex.getMessage());

        return Result.fail(ex.getMessage(), ex.getCode());
    }

    /**
     * 处理未授权异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    public Result<Void> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        logger.error("未授权异常: {}", ex.getMessage(), ex);

        // 发送WebSocket通知
        webSocketService.sendNotification("error", ex.getMessage());

        return Result.fail(ex.getMessage(), ex.getCode());
    }

    /**
     * 处理未找到资源异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public Result<Void> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        logger.error("资源未找到异常: {}", ex.getMessage(), ex);

        // 发送WebSocket通知
        webSocketService.sendNotification("warning", ex.getMessage());

        return Result.fail(ex.getMessage(), ex.getCode());
    }

    /**
     * 处理所有其他异常
     */
    @ExceptionHandler(Exception.class)
    public Result<Void> handleAllExceptions(Exception ex, WebRequest request) {
        logger.error("系统异常: {}", ex.getMessage(), ex);

        // 发送WebSocket通知
        webSocketService.sendNotification("error", "系统内部错误: " + ex.getMessage());

        return Result.fail("系统内部错误，请联系管理员", "SYSTEM_ERROR");
    }
}
