package com.SpringSecurity.SpringSecurityAppliication.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.SpringSecurity.SpringSecurityAppliication.Entity.PostEntity;
import com.SpringSecurity.SpringSecurityAppliication.Entity.User;
import com.SpringSecurity.SpringSecurityAppliication.dto.PostDto;
import com.SpringSecurity.SpringSecurityAppliication.exceptions.ResourceNotFoundException;
import com.SpringSecurity.SpringSecurityAppliication.repo.PostRepo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {

    private final PostRepo postRepo;
    private final ModelMapper modelMapper;

    @Override
    public List<PostDto> getAllPosts() {
        return postRepo
                .findAll()
                .stream()
                .map(postEntity -> modelMapper.map(postEntity, PostDto.class))
                .toList();
    }

    @Override
    public PostDto createNewPost(PostDto inputPost) {
     User user =  (User)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostEntity postEntity = modelMapper.map(inputPost, PostEntity.class);
        postEntity.setAuthor(user);
        return modelMapper.map(postRepo.save(postEntity), PostDto.class);
    }

    @Override
    public PostDto getPostById(Long postId) {
       
        PostEntity postEntity = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post not found with id:" + postId));
        return modelMapper.map(postEntity, PostDto.class);
    }

    @Override
    public PostDto updatePost(PostDto inputPost, Long postId) {

        PostEntity olderPost = postRepo.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("post not found with id:" + postId));
        inputPost.setId(postId);
        modelMapper.map(inputPost, olderPost);
        PostEntity savedEntity = postRepo.save(olderPost);
        return modelMapper.map(savedEntity, PostDto.class);

    }
}
