package com.example.appointmentservice.service;

public interface AuthService {
    /**
     * 用户登录
     * @param phone 手机号
     * @param code 验证码
     * @return 返回 Token 字符串
     */
    String login(String phone, String code);

    /**
     * 根据 Token 获取用户 ID
     * @param token Token
     * @return 用户 ID，如果无效返回 null
     */
    Long getUserIdByToken(String token);
}
