package com.smart.smartparking.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import sun.util.resources.LocaleData;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {
    private static final long serialVersionUID=1L;

    private Integer id;

    private String uid;

    private String username;

    private String password;

    private Integer state;

    @TableField(fill = FieldFill.INSERT)//插入时填充字段
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE) //插入和更新时填充字段
    private LocalDateTime updateTime;

    private String sex;

    private Integer age;

    private String phone;

    private String token;

    private String email;

    private String name;

    private String flag;

    private Integer deleted;

}
