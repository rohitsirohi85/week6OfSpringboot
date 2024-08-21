package com.SpringSecurity.SpringSecurityAppliication.services;

import java.util.List;

import com.SpringSecurity.SpringSecurityAppliication.dto.PostDto;

public interface PostService {
   List<PostDto> getAllPosts(); 

   PostDto createNewPost(PostDto inputPost);

   PostDto getPostById(Long postId);

   PostDto updatePost(PostDto inputPost , Long postId);
}
