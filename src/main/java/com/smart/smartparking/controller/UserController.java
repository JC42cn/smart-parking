package com.smart.smartparking.controller;


import com.smart.smartparking.common.JwtUtil;
import com.smart.smartparking.entity.User;
import com.smart.smartparking.service.UserService;
import com.smart.smartparking.service.impl.UserServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Slf4j
//@RequestMapping()
@RestController
public class UserController {
//    @Autowired
//    private UserService userService;

    private final String USERNAME = "admin";
    private final String PASSWORD = "123123";
    //登录
    @ApiOperation(value = "用户登录接口")
    @GetMapping("/login")
    public User login(User user) {
        log.info("登录验证");
        user.setName("user");

        if(USERNAME.equals(user.getUsername()) && PASSWORD.equals(user.getPassword())){
            log.info("登录成功");
            //添加token
            user.setToken(JwtUtil.createToken());
            log.info("............");
            return user;
        }
        return null;

    }
    @GetMapping("/checkToken")
    public Boolean checkToken(HttpServletRequest request){
        String token = request.getHeader("token");
        log.info(token+"===");
        return JwtUtil.checkToken(token);
    }

}
