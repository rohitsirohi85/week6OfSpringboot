package com.SpringSecurity.SpringSecurityAppliication.dto;

import com.SpringSecurity.SpringSecurityAppliication.Entity.enums.SubscriptionPlans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private SubscriptionPlans subscriptionPlans;
}
