package com.example.appointmentservice.controller;

import com.example.appointmentservice.dto.ApiResponse;
import com.example.appointmentservice.dto.CreateAppointmentRequest;
import com.example.appointmentservice.dto.LoginRequest;
import com.example.appointmentservice.entity.Appointment;
import com.example.appointmentservice.service.AppointmentService;
import com.example.appointmentservice.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AppointmentController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AppointmentService appointmentService;

    /**
     * 1. 登录接口
     * POST /api/login
     */
    @PostMapping("/login")
    public ApiResponse<String> login(@Valid @RequestBody LoginRequest request) {
        try {
            String token = authService.login(request.getPhone(), request.getCode());
            return ApiResponse.success(token);
        } catch (RuntimeException e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 2. 创建预约接口
     * POST /api/appointments
     * Header: Authorization: Bearer <token>
     */
    @PostMapping("/appointments")
    public ApiResponse<Appointment> createAppointment(
            @RequestHeader(value = "Authorization", required = false) String token,
            @Valid @RequestBody CreateAppointmentRequest request) {

        try {
            // 处理 Token 前缀
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            Long userId = authService.getUserIdByToken(token);
            if (userId == null) {
                return ApiResponse.error(401, "未登录或 Token 失效");
            }

            Appointment appointment = appointmentService.createAppointment(
                    userId,
                    request.getServiceName(),
                    request.getDate(),
                    request.getTimeSlot()
            );
            return ApiResponse.success(appointment);

        } catch (RuntimeException e) {
            // 捕获幂等性报错
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 3. 获取我的预约列表
     * GET /api/appointments
     * Header: Authorization: Bearer <token>
     */
    @GetMapping("/appointments")
    public ApiResponse<List<Appointment>> getMyAppointments(
            @RequestHeader(value = "Authorization", required = false) String token) {

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Long userId = authService.getUserIdByToken(token);
        if (userId == null) {
            return ApiResponse.error(401, "未登录或 Token 失效");
        }

        List<Appointment> list = appointmentService.getMyAppointments(userId);
        return ApiResponse.success(list);
    }
}

