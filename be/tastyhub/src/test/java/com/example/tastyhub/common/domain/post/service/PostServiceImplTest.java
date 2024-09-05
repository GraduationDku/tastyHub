package com.example.tastyhub.common.domain.post.service;

import static com.example.tastyhub.fixture.post.PostFixture.PAGING_POST_RESPONSES;
import static com.example.tastyhub.fixture.post.PostFixture.POST;
import static com.example.tastyhub.fixture.post.PostFixture.POST_CREATE_REQUEST;
import static com.example.tastyhub.fixture.post.PostFixture.POST_UPDATE_REQUEST;
import static com.example.tastyhub.fixture.post.PostFixture.pageable;
import static com.example.tastyhub.fixture.user.UserFixture.USER;
import static org.junit.jupiter.api.Assertions.*;

import com.example.tastyhub.common.domain.post.repository.PostRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock
    PostRepository postRepository;
    @InjectMocks
    PostServiceImpl postService;

    @Test
    @DisplayName("게시글 생성")
    void createPost() {
        postService.createPost(POST_CREATE_REQUEST, USER);
        verify(postRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("게시글 업데이트")
    void updatePost() {
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(POST));
        postService.updatePost(POST.getId(), POST_UPDATE_REQUEST, USER);
        verify(postRepository, times(1)).findById(any());

    }

    @Test
    @DisplayName("게시글 삭제")
    void deletePost() {
        postService.deletePost(POST.getId(), USER);
        verify(postRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("모든 게시글 조회")
    void getAllPost() {
        given(postRepository.findAllPostResponse(USER.getVillage(),pageable)).willReturn(
            PAGING_POST_RESPONSES);
        postService.getAllPost(USER,pageable);
        verify(postRepository, times(1)).findAllPostResponse(any(),pageable);
    }

    @Test
    @DisplayName("최신 게시글 조회")
    void getAllRecentPost() {
        given(postRepository.findAllRecentPostResponse(USER.getVillage(),pageable)).willReturn(
            PAGING_POST_RESPONSES);
        postService.getAllRecentPost(USER,pageable);
        verify(postRepository, times(1)).findAllRecentPostResponse(any(),pageable);
    }

//    @Test
//    @DisplayName("게시글 단건 조회")
//    void getPost() {
//        given(postRepository.findByIdQuery(POST.getId())).willReturn(
//            Optional.ofNullable(POST_RESPONSE));
//        postService.getPost(POST.getId());
//        verify(postRepository, times(1)).findByIdQuery(any());
//    }


    @Test
    @DisplayName("게시글 업데이트 실패")
    void updatePostFail() {
        given(postRepository.findById(any())).willThrow(new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            postService.updatePost(POST.getId(), POST_UPDATE_REQUEST, USER));
        assertEquals("해당 게시글은 존재하지 않습니다.",exception.getMessage());

    }

//    @Test
//    @DisplayName("게시글 단건 조회 실패")
//    void getPostFail() {
//        given(postRepository.findByIdQuery(any())).willThrow(new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
//            postService.getPost(POST.getId()));
//        assertEquals("해당 게시글은 존재하지 않습니다.",exception.getMessage());
//    }
}