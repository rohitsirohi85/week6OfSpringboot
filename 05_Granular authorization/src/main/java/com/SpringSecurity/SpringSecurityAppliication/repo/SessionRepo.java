package com.SpringSecurity.SpringSecurityAppliication.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SpringSecurity.SpringSecurityAppliication.Entity.Session;
import com.SpringSecurity.SpringSecurityAppliication.Entity.User;

@Repository
public interface SessionRepo extends JpaRepository<Session , Long> {

    List<Session> findByUser(User user);

    Optional<Session> findByRefreshToken(String refreshToken);
    
}
