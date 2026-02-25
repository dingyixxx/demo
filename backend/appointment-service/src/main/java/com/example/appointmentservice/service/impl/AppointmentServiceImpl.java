package com.example.appointmentservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.appointmentservice.entity.Appointment;
import com.example.appointmentservice.mapper.AppointmentMapper;
import com.example.appointmentservice.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Override
    @Transactional // 确保数据库操作的事务性
    public Appointment createAppointment(Long userId, String serviceName, LocalDate date, String timeSlot) {
        Appointment appointment = new Appointment();
        appointment.setUserId(userId);
        appointment.setServiceName(serviceName);
        appointment.setAppointmentDate(date);
        appointment.setTimeSlot(timeSlot);
        appointment.setStatus("BOOKED");

        try {
            // 利用数据库唯一索引约束 + MP 的 insert 方法
            // 如果重复，数据库会抛 DuplicateKeyException
            appointmentMapper.insert(appointment);
            return appointment;
        } catch (DuplicateKeyException e) {
            throw new RuntimeException("您已预约过该时段的服务，请勿重复提交");
        }
    }

    @Override
    public List<Appointment> getMyAppointments(Long userId) {
        // 构建查询条件：userId 匹配，并按创建时间倒序
        LambdaQueryWrapper<Appointment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Appointment::getUserId, userId)
                .orderByDesc(Appointment::getCreatedAt);

        return appointmentMapper.selectList(wrapper);
    }
}
