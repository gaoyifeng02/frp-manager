package com.gaoyifeng.exception;

/**
 * 未授权异常类
 */
public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String code, String message) {
        super(code, message);
    }

    public UnauthorizedException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }
}
