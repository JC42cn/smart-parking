package com.smart.smartparking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.smartparking.controller.domain.UserRequest;
import com.smart.smartparking.controller.domain.UserVo;
import com.smart.smartparking.entity.Permission;
import com.smart.smartparking.entity.User;

import java.util.List;

public interface UserService extends IService<User> {

    UserVo login(User user);

    List<Permission> getPermission(String flag);

//    void checkPassword(UserRequest userRequest);

    User saveUser(User user);

    User findUserByUsername(String username);


    User findUserByNameAndPSW(String username,String password);

    User findUserByUid (Long uid);

    void passwordChange(UserRequest userRequest);

    Integer selectUserNumber();

}
