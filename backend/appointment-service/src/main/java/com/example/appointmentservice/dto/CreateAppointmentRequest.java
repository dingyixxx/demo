package com.example.appointmentservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateAppointmentRequest {

    @NotBlank(message = "服务名称不能为空")
    private String serviceName;

    @NotNull(message = "日期不能为空")
    private LocalDate date;

    @NotBlank(message = "时间段不能为空")
    private String timeSlot;
}
