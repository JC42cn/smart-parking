package com.smart.smartparking.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import lombok.Data;

import java.io.Serializable;

@Data
public class RolePermission implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer roleId;
    private Integer permissionId;
}
