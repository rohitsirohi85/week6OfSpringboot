package com.SpringSecurity.SpringSecurityAppliication.Entity;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Permissions;
import com.SpringSecurity.SpringSecurityAppliication.Entity.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @ElementCollection(fetch = FetchType.EAGER)  // we want to fetch roles as soon as we use user
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

    @ElementCollection(fetch = FetchType.EAGER)  // we want to fetch permissions as soon as we use user
    @Enumerated(EnumType.STRING)
    private Set<Permissions> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities =  roles.stream().map(role->new SimpleGrantedAuthority("ROLE_"+role.name()))
        .collect(Collectors.toSet());  // toSet bcz our role is set , use toList when we use list instead of set

        permissions.forEach(
            permission -> authorities.add(new SimpleGrantedAuthority(permission.name()))
        );
        return authorities;
    }

    @Override
    public String getPassword() {
       return this.password;
    }

    @Override
    public String getUsername() {
       return this.email;
    }
    
}