package com.smart.smartparking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.smartparking.controller.domain.UserRequest;
import com.smart.smartparking.entity.Permission;
import com.smart.smartparking.entity.User;
import com.smart.smartparking.vo.UserVo;

import java.util.List;

public interface UserService extends IService<User> {

    UserVo login(User user);

    List<Permission> getPermission(String flag);

    void checkPassword(UserRequest userRequest);

    User saveUser(User user);

    void logout(String uid);

    void register(UserRequest user);
}
