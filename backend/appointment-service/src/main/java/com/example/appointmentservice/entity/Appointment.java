package com.example.appointmentservice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("appointments") // 【新增】明确指定数据库表名
public class Appointment {

    @TableId(type = IdType.AUTO) // 【新增】指定主键为自增
    private Long id;

    private Long userId;
    private String serviceName;
    private LocalDate appointmentDate;
    private String timeSlot;
    private String status;
    private LocalDateTime createdAt;
}
