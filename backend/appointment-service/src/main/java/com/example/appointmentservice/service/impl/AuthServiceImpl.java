package com.example.appointmentservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.appointmentservice.entity.User;
import com.example.appointmentservice.mapper.UserMapper;
import com.example.appointmentservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    // 模拟 Token 存储 (Key: token, Value: userId)
    // 生产环境请替换为 Redis
    private final Map<String, Long> tokenStore = new HashMap<>();

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

        // 4. 生成 Token
        String token = UUID.randomUUID().toString();
        tokenStore.put(token, user.getId());

        return token;
    }

    @Override
    public Long getUserIdByToken(String token) {
        if (token == null) return null;
        return tokenStore.get(token);
    }
}
