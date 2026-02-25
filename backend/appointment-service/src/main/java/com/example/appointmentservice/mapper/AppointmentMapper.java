package com.example.appointmentservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.appointmentservice.entity.Appointment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AppointmentMapper extends BaseMapper<Appointment> {
}
