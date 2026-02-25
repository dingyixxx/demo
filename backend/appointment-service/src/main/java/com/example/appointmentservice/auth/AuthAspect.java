package com.example.appointmentservice.auth;

import com.example.appointmentservice.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 统一的登录鉴权切面：
 * - 从请求头读取 Authorization
 * - 解析 Bearer token
 * - 调用 AuthService 获取 userId
 * - 未登录则抛出 UnauthorizedException
 * - 已登录则把 userId 放到 UserContext 中
 */
@Aspect
@Component
public class AuthAspect {

    @Autowired
    private AuthService authService;

    @Around("@annotation(com.example.appointmentservice.auth.RequireLogin)")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            throw new UnauthorizedException("未登录或 Token 失效");
        }

        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Long userId = authService.getUserIdByToken(token);
        if (userId == null) {
            throw new UnauthorizedException("未登录或 Token 失效");
        }

        try {
            UserContext.setUserId(userId);
            return joinPoint.proceed();
        } finally {
            UserContext.clear();
        }
    }
}

