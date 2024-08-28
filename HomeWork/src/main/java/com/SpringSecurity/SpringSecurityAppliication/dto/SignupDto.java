package com.SpringSecurity.SpringSecurityAppliication.dto;

import java.util.Set;

import com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Permissions;
import com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Role;
import com.SpringSecurity.SpringSecurityAppliication.Entity.enums.SubscriptionPlans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SignupDto {
    private String email;
    private String password;
    private String name;
    private Set<Role>roles;  // only used here for  learning purpose this is not recommended to use roles inside signup
    private Set<Permissions>permissions;  // only used here for  learning purpose this is not recommended 
    private SubscriptionPlans subscriptionPlans;
}
