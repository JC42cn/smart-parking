package com.smart.smartparking.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smart.smartparking.common.Result;
import com.smart.smartparking.entity.Parking;
import com.smart.smartparking.entity.Role;
import com.smart.smartparking.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/role")
@Api(tags = "角色管理")
public class RoleController {

    @Resource
    private RoleService roleService;

    @ApiOperation(value = "停车场列表", notes = "停车场列表")
    @GetMapping("/all")
    public Result findAll() {
        return Result.success(roleService.list());
    }
}
