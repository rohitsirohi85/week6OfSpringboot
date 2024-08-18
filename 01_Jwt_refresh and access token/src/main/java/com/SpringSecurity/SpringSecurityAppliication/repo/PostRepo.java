package com.SpringSecurity.SpringSecurityAppliication.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.SpringSecurity.SpringSecurityAppliication.Entity.PostEntity;

@Repository
public interface PostRepo extends JpaRepository<PostEntity , Long> {
    
}
