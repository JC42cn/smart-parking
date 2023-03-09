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
            user.setToken(JwtUtil.createToken());
            log.info("............");
            return  Result.success(user2);
        }
        return  Result.error("登录失败");

    }
    @GetMapping("/checkToken")
    public Boolean checkToken(HttpServletRequest request){
        String token = request.getHeader("token");
        log.info(token+"===");
        return JwtUtil.checkToken(token);
    }
//    @GetMapping("/login")
//    public String Login(@RequestBody UserVo uservo) {
//        //创建一个条件构造器
//        QueryWrapper<User> QueryWrapper = new QueryWrapper<>();
//        //传入查询条件
//        String username=uservo.getUsername();
//        String password=uservo.getPassword();
//        log.info(username);
//        QueryWrapper.eq("username",username).eq("password", password);
//        User user = userService.getOne(QueryWrapper);
//        if (user != null) {
//            String res = JwtUtils.sign(username, uservo.getUserId());
//            System.out.println(res);
//            return res;
//        }
//
//        return "失败";
//
//    }


}
