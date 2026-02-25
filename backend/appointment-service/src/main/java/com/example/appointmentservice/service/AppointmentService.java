package com.example.appointmentservice.service;

import com.example.appointmentservice.entity.Appointment;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {

    /**
     * 创建预约 (核心：幂等性处理)
     */
    Appointment createAppointment(Long userId, String serviceName, LocalDate date, String timeSlot);

    /**
     * 获取当前用户的所有预约
     */
    List<Appointment> getMyAppointments(Long userId);
}
