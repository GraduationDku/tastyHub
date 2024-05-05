package com.example.tastyhub.common.domain.post.controller;

import static com.example.tastyhub.common.config.APIConfig.POST_API;
import static com.example.tastyhub.common.utils.HttpResponseEntity.RESPONSE_OK;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.tastyhub.common.domain.post.dtos.PostCreateRequest;
import com.example.tastyhub.common.domain.post.dtos.PostUpdateRequest;
import com.example.tastyhub.common.domain.post.service.PostService;
import com.example.tastyhub.common.dto.StatusResponse;
import com.example.tastyhub.common.utils.Jwt.UserDetailsImpl;

import jakarta.persistence.PostUpdate;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping(POST_API)
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    
    @PostMapping("/create")
    public ResponseEntity<StatusResponse> createPost(@RequestBody PostCreateRequest postCreateRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
                postService.createPost(postCreateRequest, userDetails.getUser());
        return RESPONSE_OK;
    }
    @PatchMapping("/modify/{postId}")
    public ResponseEntity<StatusResponse> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequest postUpdateRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
                postService.updatePost(postId, postUpdateRequest, userDetails.getUser());
        return RESPONSE_OK;
    }
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<StatusResponse> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.deletePost(postId,userDetails.getUser());
        return RESPONSE_OK;

    }

}