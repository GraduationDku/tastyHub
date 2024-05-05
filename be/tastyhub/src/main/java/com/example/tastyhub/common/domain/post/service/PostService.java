package com.example.tastyhub.common.domain.post.service;

import com.example.tastyhub.common.domain.post.dtos.PostCreateRequest;
import com.example.tastyhub.common.domain.post.dtos.PostUpdateRequest;
import com.example.tastyhub.common.domain.user.entity.User;

public interface PostService {

    void createPost(PostCreateRequest postCreateRequest, User user);

    void updatePost(Long postId, PostUpdateRequest postUpdateRequest, User user);

}