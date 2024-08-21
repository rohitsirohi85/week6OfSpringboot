package com.SpringSecurity.SpringSecurityAppliication.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SpringSecurity.SpringSecurityAppliication.Entity.User;

@Repository
public interface UserRepo extends JpaRepository<User , Long> {
    
     Optional<User> findByEmail(String email);

}
