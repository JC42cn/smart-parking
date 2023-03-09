package com.smart.smartparking.service.impl;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smart.smartparking.entity.Permission;
import com.smart.smartparking.entity.RolePermission;
import com.smart.smartparking.mapper.PermissionMapper;
import com.smart.smartparking.mapper.RolePermissionMapper;
import com.smart.smartparking.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Resource
    RolePermissionMapper rolePermissionMapper;


    @Override
    public List<Permission> tree() {
        List<Permission> allData = list();

        return childrenTree(null,allData);
    }

    @Override
    @Transactional
    //删除权限
    public void deletePermission(Integer id) {
        rolePermissionMapper.delete(new UpdateWrapper<RolePermission>().eq("p_id", id));
        remove(new UpdateWrapper<Permission>().set("pId", id)); // 删除子菜单
        removeById(id);

    }
    // 递归生成树
    private List<Permission> childrenTree(Integer pId,List<Permission> allData){
        List<Permission> list = new ArrayList<>();
        for (Permission permission : allData) {
            if (Objects.equals(permission.getPId(), pId)) {  // null, 一级
                list.add(permission);
                List<Permission> childrenTree = childrenTree(permission.getId(), allData);  // 递归调用， 摘取二级节点、三级、四级...
                permission.setChildren(childrenTree);
            }
        }
        return list;
        }
}
