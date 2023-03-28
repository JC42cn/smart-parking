package com.smart.smartparking.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.smart.smartparking.entity.Permission;
import com.smart.smartparking.mapper.PermissionMapper;

import java.util.List;

public interface PermissionService extends IService<Permission> {
    List<Permission> tree();

    void deletePermission(Integer id);
}
