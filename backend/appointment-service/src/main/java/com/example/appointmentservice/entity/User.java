package com.example.appointmentservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("users") // 【新增】明确指定数据库表名
public class User {

    @TableId(type = IdType.AUTO) // 【新增】指定主键为自增
    private Long id;

    private String phone;
    private String name;
    private LocalDateTime createdAt;
}