package com.SpringSecurity.SpringSecurityAppliication.dto;

import java.util.Set;

import com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Role;

import lombok.Data;

@Data
public class SignupDto {
    private String email;
    private String password;
    private String name;
    private Set<Role>roles;  // only used here for  learning purpose this is not recommended to use roles inside signup
}
