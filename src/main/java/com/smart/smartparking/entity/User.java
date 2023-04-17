package com.smart.smartparking.entity;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {
    private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long uid;

    private String username;

    private String password;

   // private Integer state;

    @TableField(fill = FieldFill.INSERT)//插入时填充字段
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) //插入和更新时填充字段
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private String address;

    private String token;

    private String email;

    private String name;

    private String flag;

    // 逻辑删除 0存在  id删除
    @ApiModelProperty("逻辑删除 0存在  id删除")
    @Alias("逻辑删除 0存在  id删除")
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;

    private String avatar;

}
