package com.smart.smartparking.vo;

import com.smart.smartparking.entity.User;
import com.smart.smartparking.entity.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVo implements Serializable {
    private User user;
    private String token;
    private List<Permission> menus;
    private List<Permission> auths;
}
