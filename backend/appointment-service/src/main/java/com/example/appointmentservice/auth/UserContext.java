package com.example.appointmentservice.auth;

/**
 * 使用 ThreadLocal 保存当前请求的 userId，便于在 Controller / Service 中获取
 */
public class UserContext {

    private static final ThreadLocal<Long> USER_ID_HOLDER = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        USER_ID_HOLDER.set(userId);
    }

    public static Long getUserId() {
        return USER_ID_HOLDER.get();
    }

    public static void clear() {
        USER_ID_HOLDER.remove();
    }
}

