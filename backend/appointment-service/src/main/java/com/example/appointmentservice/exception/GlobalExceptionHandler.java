package com.example.appointmentservice.exception;

import com.example.appointmentservice.dto.ApiResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 统一处理请求体参数校验失败（例如 LoginRequest 上的 @Valid）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = "参数校验失败";

        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null && fieldError.getDefaultMessage() != null) {
            // 这里会拿到你在 LoginRequest 上配置的 message，比如“手机号格式不正确”
            message = fieldError.getDefaultMessage();
        }

        // 返回统一的响应结构给前端，code 使用 400 表示参数错误
        return ApiResponse.error(400, message);
    }
}

