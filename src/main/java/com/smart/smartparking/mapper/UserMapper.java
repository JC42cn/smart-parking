package com.smart.smartparking.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.smart.smartparking.entity.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper

public interface UserMapper extends BaseMapper<User> {
}
