package com.smart.smartparking.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.common.annotation.AutoLog;
import com.smart.smartparking.entity.Car;
import com.smart.smartparking.entity.User;
import com.smart.smartparking.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;


@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {
    @Resource
    private UserService userService;

//    @AutoLog("新增用户")
//    @PostMapping
//    @SaCheckPermission("user.add")
//    public Result insertUser(@RequestBody User user){
//        userService.save(user);
//        return Result.success();
//    }

    @AutoLog("新增用户")
    @PostMapping
    //@SaCheckPermission("user.add")
    public Result save(@RequestBody User user) {

        userService.save(user);
        return Result.success();
    }

    @AutoLog("编辑用户")
    @PutMapping
    //@SaCheckPermission("user.edit")
    public Result update(@RequestBody User user) {
        userService.updateById(user);
        return Result.success();
    }
    @ApiOperation(value = "删除用户", notes = "删除用户")
    @AutoLog("删除用户")
    @DeleteMapping("/{id}")
    //@SaCheckPermission("user.delete")
    public Result delete(@PathVariable Integer id) {
        userService.removeById(id);
        return Result.success("删除用户成功");
    }

    @AutoLog("批量删除用户")
    @PostMapping("/del/batch")
    //@SaCheckPermission("user.deleteBatch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        userService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
   // @SaCheckPermission("user.list")
    public Result findAll() {
        return Result.success(userService.list());
    }

    @GetMapping("/{id}")
    //@SaCheckPermission("user.list")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(userService.getById(id));
    }

    @GetMapping("/page")
    //@SaCheckPermission("user.list")
    public Result findPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "name", name);
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }


    @AutoLog("编辑用户")
    @PutMapping("/updateUser")
    public Result updateUser(@RequestBody User user) {
//        Object loginId = StpUtil.getLoginId();
//        log.info(loginId.toString());
//        log.info(user.getUid());

//        if (!loginId.equals(user.getUid())) {
//            Result.error("无权限");
//        }
        log.info(user.toString());
        userService.updateById(user);
        return Result.success(user);
    }

    @GetMapping("/alluser")
    public Result findUserPage(@RequestParam(defaultValue = "") String name,
                           @RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "name", name).eq("flag","USER");
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/alladmin")
    public Result findAdminPage(@RequestParam(defaultValue = "") String name,
                               @RequestParam Integer pageNum,
                               @RequestParam Integer pageSize) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>().orderByDesc("id");
        queryWrapper.like(!"".equals(name), "name", name).eq("flag","ADMIN");
        return Result.success(userService.page(new Page<>(pageNum, pageSize), queryWrapper));
    }

    @GetMapping("/selectUserNumber")
    public Result selectUserNumber() {
        int userNumber = userService.selectUserNumber();
        return Result.success(userNumber);
    }



}
