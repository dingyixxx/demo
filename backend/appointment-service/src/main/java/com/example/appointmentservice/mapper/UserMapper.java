package com.example.appointmentservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.appointmentservice.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
