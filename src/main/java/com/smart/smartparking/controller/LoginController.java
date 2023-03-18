package com.smart.smartparking.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.common.JwtUtil;
import com.smart.smartparking.entity.User;
import com.smart.smartparking.service.UserService;
import com.smart.smartparking.vo.UserVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        log.info("登录验证")   ;
        log.info("账号"+user.getUsername()+user.getPassword());
        UserVo user2 = userService.login(user);

        if(user2!=null){
            log.info("登录成功");
            //添加token
            user2.setToken(JwtUtil.createToken());
            log.info("登录成功 t添加token"+user.getToken());
            return  Result.success(user2);
        }
        return  Result.error("登录失败");

    }

    @ApiOperation(value = "用户退出登录接口")
    @GetMapping("/logout/{uid}")
    public Result logout(@PathVariable String uid){
        userService.logout(uid);
        return Result.success();
    }

    @GetMapping("/checkToken")
    public Boolean checkToken(HttpServletRequest request){
        String token = request.getHeader("token");
        log.info(token+"===");
        return JwtUtil.checkToken(token);
    }
}
