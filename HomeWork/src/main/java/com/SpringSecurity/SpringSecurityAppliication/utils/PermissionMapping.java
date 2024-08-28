package com.SpringSecurity.SpringSecurityAppliication.utils;

import static com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Permissions.POST_CREATE;
import static com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Permissions.POST_DELETE;
import static com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Permissions.POST_UPDATE;
import static com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Permissions.POST_VIEW;
import static com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Permissions.USER_CREATE;
import static com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Permissions.USER_DELETE;
import static com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Permissions.USER_UPDATE;
import static com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Permissions.USER_VIEW;
import static com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Role.*;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Permissions;
import com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Role;

public class PermissionMapping {
    private static final Map<Role , Set<Permissions>> map = Map.of(
             USER , Set.of(USER_VIEW,POST_VIEW),
             CREATOR,Set.of(POST_CREATE,USER_UPDATE,POST_UPDATE),
             ADMIN,Set.of(POST_CREATE,USER_UPDATE,POST_UPDATE,USER_CREATE,USER_DELETE,POST_DELETE)
    );
    public static Set<SimpleGrantedAuthority> getAuthForRoles(Role role){
        return map.get(role).stream().map(perm->new SimpleGrantedAuthority(perm.name()))
        .collect(Collectors.toSet());
    }
}
