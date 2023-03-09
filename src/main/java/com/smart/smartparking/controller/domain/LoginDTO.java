package com.smart.smartparking.controller.domain;

import com.smart.smartparking.entity.Permission;
import com.smart.smartparking.entity.User;
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
public class LoginDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private User user;
    private String token;
    private List<Permission> menus;
    private List<Permission> auths;
}
