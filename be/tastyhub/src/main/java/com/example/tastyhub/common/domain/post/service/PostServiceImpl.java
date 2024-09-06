package com.example.tastyhub.common.domain.post.service;

import com.example.tastyhub.common.domain.post.dtos.PagingPostResponse;
import com.example.tastyhub.common.domain.post.dtos.PostResponse;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        Post post = Post.createPost(postCreateRequest.getTitle(),postCreateRequest.getContent(),PostState.Start,user);
        postRepository.save(post);
    }

    @Override
    public void updatePost(Long postId, PostUpdateRequest postUpdateRequest, User user) {
        Post post = findById(postId);
        post.update(postUpdateRequest);
    }

    private Post findById(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));
    }

    @Override
    public void deletePost(Long postId, User user) {
        postRepository.deleteById(postId);
    }

    @Override
    public Page<PagingPostResponse> getAllPost(User user, Pageable pageable) {
        return postRepository.findAllPostResponse(
            user.getVillage(),pageable);
    }

    @Override
    public Page<PagingPostResponse> getAllRecentPost(User user, Pageable pageable) {
        return postRepository.findAllRecentPostResponse(
            user.getVillage(), pageable);
    }

    @Override
    public PostResponse getPost(Long postId) {
        return getPostFindByPostId(postId);
    }

    @Generated
    private PostResponse getPostFindByPostId(Long postId) {

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));
        return PostResponse.createPostResponse(post);

    }


}