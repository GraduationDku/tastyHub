package com.example.tastyhub.common.domain.post.service;

import com.example.tastyhub.common.domain.post.dtos.PagingPostResponse;
import com.example.tastyhub.common.domain.post.dtos.PostResponse;
import java.util.List;
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

    @Override
    public void deletePost(Long postId, User user) {
        postRepository.deleteById(postId);
    }

    @Override
    public List<PagingPostResponse> getAllPost(User user) {
        List<PagingPostResponse> postResponses = postRepository.findAllPostResponse(user.getVillage());
        return postResponses;
    }

    @Override
    public List<PagingPostResponse> getAllRecentPost(User user) {
        List<PagingPostResponse> postResponses = postRepository.findAllRecentPostResponse(user.getVillage());
        return postResponses;
    }

    @Override
    public PostResponse getPost(Long postId) {
        Post post = getPostFindByPostId(postId);
        PostResponse postResponse = new PostResponse(post);
        return postResponse;
    }

    private Post getPostFindByPostId(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("해당 게시물은 존재하지 않습니다."));
    }

}