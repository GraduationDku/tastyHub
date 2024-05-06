package com.example.tastyhub.common.domain.post.service;

import com.example.tastyhub.common.domain.post.dtos.PagingPostResponse;
import com.example.tastyhub.common.domain.post.dtos.PostCreateRequest;
import com.example.tastyhub.common.domain.post.dtos.PostResponse;
import com.example.tastyhub.common.domain.post.dtos.PostUpdateRequest;
import com.example.tastyhub.common.domain.user.entity.User;
import java.util.List;

public interface PostService {

    void createPost(PostCreateRequest postCreateRequest, User user);

    void updatePost(Long postId, PostUpdateRequest postUpdateRequest, User user);

    void deletePost(Long postId, User user);

    List<PagingPostResponse> getAllPost(User user);

    List<PagingPostResponse> getAllRecentPost(User user);

    PostResponse getPost(Long postId);
}