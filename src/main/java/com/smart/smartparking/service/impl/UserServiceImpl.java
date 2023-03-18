package com.smart.smartparking.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.smartparking.common.JwtUtil;
import com.smart.smartparking.controller.domain.UserRequest;
import com.smart.smartparking.entity.Permission;
import com.smart.smartparking.entity.Role;
import com.smart.smartparking.entity.RolePermission;
import com.smart.smartparking.exceprion.ServiceException;
import com.smart.smartparking.mapper.UserMapper;
import com.smart.smartparking.service.PermissionService;
import com.smart.smartparking.service.RolePermissionService;
import com.smart.smartparking.service.RoleService;
import com.smart.smartparking.service.UserService;
import com.smart.smartparking.entity.User;
import com.smart.smartparking.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.util.IdUtil;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper,User> implements UserService {

    private static final long TIME_IN_MS5 = 5 * 60 * 1000;  // 表示5分钟的毫秒数

    @Resource
    RolePermissionService rolePermissionService;

    @Resource
    RoleService roleService;

    @Resource
    PermissionService permissionService;

    @Override
    public UserVo login(User user) {
        User dbUser;
        try {
            dbUser = getOne(new UpdateWrapper<User>().eq("username", user.getUsername()));
        } catch (Exception e) {
            throw new ServiceException("数据库异常");
        }
        if (dbUser == null) {
            throw new ServiceException("未找到用户");
        }
        if (!user.getPassword().equals( dbUser.getPassword()))
        {
            throw new RuntimeException("用户名或密码错误");

        }
        String flag = dbUser.getFlag();
        List<Permission> all = getPermission(flag);  // 水平
        List<Permission> menus = getTreePermission(all); // 树
        // 页面的按钮权限集合
        List<Permission> auths = all.stream().filter(Permission -> Permission.getType() == 3).collect(Collectors.toList());
        return UserVo.builder().user(dbUser).menus(menus).auths(auths).build();
    }

    @Override
    public List<Permission> getPermission(String flag) {
        Role role = roleService.getOne(new QueryWrapper<Role>().eq("flag", flag));
        List<RolePermission> rolePermissions = rolePermissionService.list(new QueryWrapper<RolePermission>().eq("role_id",role.getId()));
        List<Integer> PermissionIds = rolePermissions.stream().map(RolePermission::getPermissionId).collect(Collectors.toList());
        List<Permission> PermissionList = permissionService.list();
        List<Permission> all = new ArrayList<>();//水平树菜单
         for (Integer PermissionId : PermissionIds) {
            PermissionList.stream().filter(Permission -> Permission.getId().equals(PermissionId)).findFirst()
                    .ifPresent(all::add);
        }
        return all;
    }
    //校验密码
    @Override
    public void checkPassword(UserRequest userRequest) {
        User dbUser = getOne(new UpdateWrapper<User>().eq("uid", userRequest.getUid()));
        if (dbUser == null) {
            throw new ServiceException("未找到用户");
        }
        boolean checkPSD = BCrypt.checkpw(userRequest.getPassword(), dbUser.getPassword());
        if (!checkPSD) {
            throw new ServiceException("原密码错误");
        }
        String newPass = userRequest.getNewPassword();
        dbUser.setPassword(BCrypt.hashpw(newPass));
        updateById(dbUser);   // 设置到数据库
    }

    @Override
    public User saveUser(User user) {
        User dbUser = getOne(new UpdateWrapper<User>().eq("username", user.getUsername()));
        if (dbUser != null) {
            throw new ServiceException("用户已存在");
        }
        // 设置昵称
        if (StrUtil.isBlank(user.getName())) {
//                String name = Constants.USER_NAME_PREFIX + DateUtil.format(new Date(), Constants.DATE_RULE_YYYYMMDD)
//                        + RandomUtil.randomString(4);
            user.setName("系统用户" + RandomUtil.randomString(6));
        }
        if (StrUtil.isBlank(user.getPassword())) {
            user.setPassword("123123");   // 设置默认密码
        }
        // 加密用户密码
        //user.setPassword(BCrypt.hashpw(user.getPassword()));  // BCrypt加密
        // 设置唯一标识
        user.setUid(IdUtil.fastSimpleUUID());
        try {
            save(user);
        } catch (Exception e) {
            throw new RuntimeException("注册失败", e);
        }
        return user;
    }


    @Override
    public void logout(String uid) {
        StpUtil.logout(uid);
        log.info("用户{}退出成功",uid);
    }

    @Override
    public void register(UserRequest user) {

    }


    // 获取角色对应的菜单树
    private List<Permission> getTreePermission(List<Permission> all) {
        // 菜单树 1级 -> 2级
        List<Permission> parentList = all.stream().filter(Permission -> Permission.getType() == 1
                || (Permission.getType() == 2 && Permission.getPId() == null)).collect(Collectors.toList());// type==1 是目录  或者  pid = null
        for (Permission Permission : parentList) {
            Integer pid = Permission.getId();
            List<Permission> level2List = all.stream().filter(Permission1 -> pid.equals(Permission.getPId())).collect(Collectors.toList());// 2级菜单
            Permission.setChildren(level2List);
        }
        return parentList.stream().sorted(Comparator.comparing(Permission::getOrders)).collect(Collectors.toList());  // 排序
    }


}
