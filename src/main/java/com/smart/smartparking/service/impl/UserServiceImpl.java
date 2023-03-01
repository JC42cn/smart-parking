package com.smart.smartparking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.smartparking.mapper.UserMapper;
import com.smart.smartparking.service.UserService;
import com.smart.smartparking.entity.User;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {
}
