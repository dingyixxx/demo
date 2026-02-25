package com.example.appointmentservice.auth;

/**
 * 未登录或 token 失效时抛出的异常，交给全局异常处理返回 401
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}

