package com.gaoyifeng.exception;

/**
 * 资源未找到异常类
 */
public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String code, String message) {
        super(code, message);
    }

    public ResourceNotFoundException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
