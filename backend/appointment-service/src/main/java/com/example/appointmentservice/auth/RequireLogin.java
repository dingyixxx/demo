package com.example.appointmentservice.auth;

import java.lang.annotation.*;

/**
 * 标记需要登录鉴权的方法（通常是 Controller 接口）
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequireLogin {
}

