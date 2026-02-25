package com.example.appointmentservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.appointmentservice.entity.User;
import com.example.appointmentservice.mapper.UserMapper;
import com.example.appointmentservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    // Token 有效期（分钟）——当前按你的要求设为 1 分钟，方便测试
    private static final long TOKEN_TTL_MINUTES = 600L;

    /**
     * 模拟 Token 存储 (Key: token, Value: TokenInfo)
     * 生产环境请替换为 Redis / JWT 等
     */
    private final Map<String, TokenInfo> tokenStore = new HashMap<>();

    private static class TokenInfo {
        private final Long userId;
        private final LocalDateTime expiresAt;

        private TokenInfo(Long userId, LocalDateTime expiresAt) {
            this.userId = userId;
            this.expiresAt = expiresAt;
        }
    }

    @Override
    public String login(String phone, String code) {
        // 1. 验证验证码 (写死 123456)
        if (!"123456".equals(code)) {
            throw new RuntimeException("验证码错误");
        }

        // 2. 查询用户是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getPhone, phone);
        User user = userMapper.selectOne(wrapper);

        // 3. 如果不存在，自动注册
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setName("User_" + phone.substring(Math.max(0, phone.length() - 4))); // 简单生成名字
            userMapper.insert(user); // MP 自动插入并回填 ID
        }

        // 4. 生成 Token，并设置过期时间
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(TOKEN_TTL_MINUTES);
        tokenStore.put(token, new TokenInfo(user.getId(), expiresAt));

        return token;
    }

    @Override
    public Long getUserIdByToken(String token) {
        if (token == null) return null;

        TokenInfo info = tokenStore.get(token);
        if (info == null) {
            return null;
        }

        // 判断是否过期
        if (LocalDateTime.now().isAfter(info.expiresAt)) {
            // 过期后移除 token
            tokenStore.remove(token);
            return null;
        }

        return info.userId;
    }
}
