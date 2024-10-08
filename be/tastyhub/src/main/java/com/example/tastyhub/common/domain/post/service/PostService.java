package com.example.tastyhub.common.domain.post.service;

import com.example.tastyhub.common.domain.post.dtos.PagingPostResponse;
import com.example.tastyhub.common.domain.post.dtos.PostCreateRequest;
import com.example.tastyhub.common.domain.post.dtos.PostResponse;
import com.example.tastyhub.common.domain.post.dtos.PostUpdateRequest;
import com.example.tastyhub.common.domain.user.entity.User;
import com.example.tastyhub.common.utils.page.RestPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    void createPost(PostCreateRequest postCreateRequest, User user);

    void updatePost(Long postId, PostUpdateRequest postUpdateRequest, User user);

    void deletePost(Long postId, User user);

    RestPage<PagingPostResponse> getAllPost(User user, Pageable pageable);

    Page<PagingPostResponse> getAllRecentPost(User user, Pageable pageable);

    PostResponse getPost(Long postId);
}