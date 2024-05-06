package com.example.tastyhub.common.domain.post.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.tastyhub.common.domain.post.repository.PostRepository;
import com.example.tastyhub.common.domain.post.repository.PostRepositoryQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    PostRepository postRepository;
    @InjectMocks
    PostServiceImpl postService;

    @Test
    @DisplayName("게시글 생성")
    void createPost() {
    }

    @Test
    @DisplayName("게시글 업데이트")
    void updatePost() {
    }

    @Test
    @DisplayName("게시글 삭제")
    void deletePost() {
    }

    @Test
    @DisplayName("모든 게시글 조회")
    void getAllPost() {
    }

    @Test
    @DisplayName("최신 게시글 조회")
    void getAllRecentPost() {
    }

    @Test
    @DisplayName("게시글 단건 조회")
    void getPost() {
    }

    @Test
    @DisplayName("게시글 생성 실패")
    void createPostFail() {
    }

    @Test
    @DisplayName("게시글 업데이트 실패")
    void updatePostFail() {
    }

    @Test
    @DisplayName("게시글 삭제 실패")
    void deletePostFail() {
    }

    @Test
    @DisplayName("모든 게시글 조회 실패")
    void getAllPostFail() {
    }

    @Test
    @DisplayName("최신 게시글 조회 실패")
    void getAllRecentPostFail() {
    }

    @Test
    @DisplayName("게시글 단건 조회 실패")
    void getPostFail() {
    }
}