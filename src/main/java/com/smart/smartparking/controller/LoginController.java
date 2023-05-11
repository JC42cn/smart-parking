package com.smart.smartparking.controller;


import com.smart.smartparking.common.Result;
import com.smart.smartparking.common.JwtUtil;
import com.smart.smartparking.common.annotation.AutoLog;
import com.smart.smartparking.controller.domain.UserRequest;
import com.smart.smartparking.controller.domain.UserVo;
import com.smart.smartparking.entity.User;
import com.smart.smartparking.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@Controller
@Slf4j
@RestController
public class LoginController {

    @Resource
    private UserService userService;

    //登录
    @ApiOperation(value = "用户登录接口")
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        user = userService.findUserByNameAndPSW(user.getUsername(),user.getPassword());
        if (user==null){
            return Result.error("800","该用户不存在");
        }else{
            UserVo user2 = userService.login(user);
            if(user2!=null){
                //添加token
                user2.setToken(JwtUtil.createToken());
                log.info("登录成功 t添加token"+user.getToken());
                return  Result.success(user2);
            }
            return  Result.error("登录失败");
        }

    }



    //注册
    @ApiOperation(value = "用户注册接口")
    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        String username = user.getUsername();
        User u = userService.findUserByUsername(username);
        long timestamp = System.currentTimeMillis();
        if (u==null){
            user.setFlag("USER");
            user.setUid(timestamp);
            userService.save(user);
            return Result.success();
        }else {
            return  Result.error("该账号已存在！");
        }
    }

    @AutoLog("用户修改密码")
    @PostMapping("/password/change")
    public Result passwordChange(@RequestBody UserRequest userRequest) {
        userService.passwordChange(userRequest);
        return Result.success();
    }


    @GetMapping("/checkToken")
    public Boolean checkToken(HttpServletRequest request){
        String token = request.getHeader("token");
        log.info(token+"===");
        return JwtUtil.checkToken(token);
    }
}
