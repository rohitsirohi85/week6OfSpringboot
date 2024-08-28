package com.SpringSecurity.SpringSecurityAppliication.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.SpringSecurity.SpringSecurityAppliication.Entity.User;
import com.SpringSecurity.SpringSecurityAppliication.Entity.enums.SubscriptionPlans;

@Component
public class SubscriptionService {
    
     public boolean hasAccessBasedPlan(String requiredPlan){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SubscriptionPlans userPlans = user.getSubscriptionPlans();
          return userPlans.name().equals(requiredPlan);
     }

}
