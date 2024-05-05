package com.example.tastyhub.common.domain.post.service;

import org.springframework.stereotype.Service;

import com.example.tastyhub.common.domain.post.dtos.PostCreateRequest;
import com.example.tastyhub.common.domain.post.dtos.PostUpdateRequest;
import com.example.tastyhub.common.domain.post.entity.Post;
import com.example.tastyhub.common.domain.post.entity.Post.PostState;
import com.example.tastyhub.common.domain.post.repository.PostRepository;
import com.example.tastyhub.common.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public void createPost(PostCreateRequest postCreateRequest, User user) {
        Post post = Post.builder().title(postCreateRequest.getTitle()).text(postCreateRequest.getText())
                .postState(PostState.Start).user(user).build();
        postRepository.save(post);
    }

    @Override
    public void updatePost(Long postId, PostUpdateRequest postUpdateRequest, User user) {
        Post post = postRepository.findById(postId).get();
        post.update(postUpdateRequest);
    }

}