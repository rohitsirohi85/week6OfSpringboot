package com.SpringSecurity.SpringSecurityAppliication.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.SpringSecurity.SpringSecurityAppliication.Entity.User;
import com.SpringSecurity.SpringSecurityAppliication.dto.PostDto;
import com.SpringSecurity.SpringSecurityAppliication.services.PostService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostSecurity {

    private final PostService postService;
    
     public Boolean isOwnerOfPost(Long postId){
          User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostDto post = postService.getPostById(postId);
        return post.getAuthor().getId().equals(user.getId());

      }

}
