package com.SpringSecurity.SpringSecurityAppliication.controller;

import java.util.List;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.SpringSecurity.SpringSecurityAppliication.dto.PostDto;
import com.SpringSecurity.SpringSecurityAppliication.services.PostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    @Secured({"ROLE_USER","ROLE_ADMIN"})  // so we add a logic here that this endpoint can only be accessible by user and admin role
    public List<PostDto> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping
    public PostDto createNewPost(@RequestBody PostDto inputPost) {
        return postService.createNewPost(inputPost);
    }
    
    @GetMapping(path = "/{postId}")
   // @PreAuthorize("hasAnyRole('USER' , 'ADMIN')") // only given roles can access this endpoint
   // @PreAuthorize("hasAnyRole('USER' , 'ADMIN') OR hasAuthority('POST_VIEW')") // either role is user or admin either authority is POST_VIEW
      @PreAuthorize("@postSecurity.isOwnerOfPost(#postId)") // we made this method inside postSecurity utils (Note:  when calling the bean first letter is small like @postSecurity)
    public PostDto getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }

    @PutMapping(path = "/{postId}")
    public PostDto updatePost(@RequestBody PostDto inputPost, @PathVariable Long postId) {
        return postService.updatePost(inputPost, postId);
    }

    @GetMapping(path = "/freePlan")
    @PreAuthorize("@subscriptionService.hasAccessBasedPlan('FREE')") // so this endpoint can only be access by user who has free plan when he signed up(note: when you sign up you have to give subscription plan there and use that access token to authorize this route if user contain this plan route success otherwise forbidden error)
    public String SeeFreePlans(){
        return "this is a free plan which contain 1 session only";
    }
    @GetMapping(path = "/basicPlan")
    @PreAuthorize("@subscriptionService.hasAccessBasedPlan('BASIC')")
    public String SeeBasicPlans(){
        return "this is a basic plan which contain 2 session only";
    }
    @GetMapping(path = "/premiumPlan")
    @PreAuthorize("@subscriptionService.hasAccessBasedPlan('PREMIUM')")
    public String SeePremiumPlans(){
        return "this is a premium plan which contain 3 session only";
    }
}
