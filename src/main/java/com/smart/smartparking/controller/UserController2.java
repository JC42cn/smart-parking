//package com.smart.smartparking.controller;
//
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.lin.garbagesorting.common.R;
//import com.lin.garbagesorting.entity.User;
//import com.lin.garbagesorting.service.UserService;
//import com.lin.garbagesorting.utils.StringUtil;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.util.DigestUtils;
//import org.springframework.web.bind.annotation.*;
//
//import javax.servlet.http.HttpServletRequest;
//import java.time.LocalDateTime;
//
//@Controller
//@Slf4j
//@RestController
//@RequestMapping("user")
//@Api(tags = "API基础用户管理")
//public class UserController {
//    @Autowired
//    UserService userService;
//
//
//
//
//    @ApiOperation(value = "查询用户", notes = "查询用户", response = User.class)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "username", value = "用户名", required = false, dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "name", value = "姓名", required = false, dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "password", value = "密码", required = false, dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "phone", value = "手机号", required = false, dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "sex", value = "性别", required = false, dataType = "String", paramType = "query"),
//            @ApiImplicitParam(name = "status", value = "状态", required = false, dataType = "int", paramType = "query"),
//            @ApiImplicitParam(name = "type", value = "类型", required = false, dataType = "int", paramType = "query")
//    })
//    @PostMapping
//    public R<String> add(@RequestBody User user, HttpServletRequest request){
//        log.info("新增员工{}",user.toString());
//        if(StringUtil.isNotNull(user.getPassword())){
//            user.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        }
//        user.setStatus(1);
//        LambdaQueryWrapper<User> qWrapper = new LambdaQueryWrapper();
//        //添加过滤条件
//        qWrapper.eq(StringUtils.isNotEmpty(user.getUsername()),User::getUsername,user.getUsername());
//        if(StringUtil.isNotNull(userService.getOne(qWrapper))){
//            return R.error("用户名已存在");
//        }
//        userService.save(user);
//        return R.success("新增用户成功 ");
//    }
//
//    @ApiOperation(value = "删除用户", notes = "删除用户", response = User.class)
//    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "query")
//    @DeleteMapping
//    public R delete(Long id){
//        log.info("删除用户，id为：{}",id);
//        userService.removeById(id);
//        return R.success("用户信息删除成功");
//    }
//
//
//
//    @ApiOperation(value = "修改用户", notes = "修改用户", response = User.class)
//    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "query")
//    @PutMapping
//    public R<String> update(@RequestBody User user,HttpServletRequest request){
//        log.info(user.toString());
//        userService.updateById(user);
//        return R.success("员工信息修改成功");
//    }
//
//    @GetMapping("/page")
//    public R<Page> page(int page, int pageSize, String name){
//        log.info("page = {},pageSize = {},name = {}" ,page,pageSize,name);
//
//        //构造分页构造器
//        Page pageInfo = new Page(page,pageSize);
//
//        //构造条件构造器
//        LambdaQueryWrapper<User> qWrapper = new LambdaQueryWrapper();
//        //添加过滤条件
//        qWrapper.like(StringUtils.isNotEmpty(name),User::getName,name);
//        //添加排序条件
//        qWrapper.orderByDesc(User::getUpdateTime);
//
//        //执行查询
//        userService.page(pageInfo,qWrapper);
//
//        return R.success(pageInfo);
//    }
//
//
//    @ApiOperation(value = "查询用户", notes = "查询用户", response = User.class)
//    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", paramType = "query")
//    @GetMapping("/{username}")
//    public R<User> getByUsername(@PathVariable String username){
//        LambdaQueryWrapper<User> qWrapper = new LambdaQueryWrapper();
//        //添加过滤条件
//        qWrapper.eq(StringUtils.isNotEmpty(username),User::getUsername,username);
//        User user = userService.getOne(qWrapper);
//        if(StringUtil.isNotNull(user)){
//            return R.success(user);
//        }
//        return R.error("没有查到对应用户");
//    }
//    @RequestMapping("/hello")
//    public String hello(){
//        return "1";
//    }
//
//}
