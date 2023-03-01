package com.smart.smartparking.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.smartparking.common.R;
import com.smart.smartparking.entity.User;
import com.smart.smartparking.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/user")

public class UserController {
    @Autowired
    private UserService userService;

    //用户登录

    @PostMapping("/login")
    public R<User> login(HttpServletRequest request, @RequestBody User user) {
        String password = user.getPassword();
        //DigestUtils.md5DigestAsHex(password.getBytes());

        //name查询数据库
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUsername,user.getUsername());

        User use = userService.getOne(userLambdaQueryWrapper);//getOne()


        //判断是否查询到
        if (use == null){
         return R.error("登录失败");
        }
        //密码验证
        if (!use.getPassword().equals(password)){
         return R.error("密码错误");
        }
        //验证用户账号状态
        if (use.getState()==0){
            return R.error("账号已禁用");
        }

        //登录成功，用户id存入Session并返回登录成功结果
       // request.getSession().setAttribute("user",use.getId());

        return R.success(use);
    }

    //用户添加
    @PostMapping
    public R<String> add(@RequestBody User user, HttpServletRequest request){
        log.info("新增用户{}，用户信息",user.toString());
        user.setCreateTime(LocalDateTime.now());
        try{
            userService.save(user);
        }catch (Exception ex){
            R.error("新增用户失败");
        }
        return R.success("新增用户成功 ");
    }

    //用户删除
    @DeleteMapping
    public R delete(int id){
        log.info("删除用户，id为：{}",id);
        userService.removeById(id);
        return R.success("用户信息删除成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){//name是搜索的内容
        log.info("page = {},pageSize = {},name = {}" ,page,pageSize,name);

        //构造分页构造器
        Page pageInfo = new Page(page,pageSize);

        //构造条件构造器
        LambdaQueryWrapper<User> qWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        qWrapper.like(StringUtils.isNotEmpty(name),User::getName,name);//isNotEmpty(name) name不等于空
        //添加排序条件
       // qWrapper.orderByDesc(User::getCrateTime);

        //执行查询
        userService.page(pageInfo,qWrapper);

        return R.success(pageInfo);
    }

    @PutMapping
    public R<String> update(@RequestBody User user,HttpServletRequest request){
        log.info(user.toString());
       // user.setUserState(1); //修改用户状态
        //user.setCrateTime(LocalDateTime.now());//修改用户时间
        request.getSession().getAttribute("user");
        userService.updateById(user);
        return R.success("信息修改成功");
    }


    //根据id查询员工信息
    @GetMapping("/{id}")
    public R<User> getById(@PathVariable int id){
        log.info("根据id查询用户信息");

        User user = userService.getById(id);

        if (user!=null){
            return R.success(user);
        }
        return R.error("没有查询到对应用户信息");
    }

}
