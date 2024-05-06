package com.example.tastyhub.common.domain.comment.service;

import static com.example.tastyhub.fixture.comment.CommentFixture.COMMENT;
import static com.example.tastyhub.fixture.comment.CommentFixture.COMMENT_REQUEST;
import static com.example.tastyhub.fixture.post.PostFixture.POST;
import static com.example.tastyhub.fixture.user.UserFixture.USER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.tastyhub.common.domain.comment.repository.CommentRepository;
import com.example.tastyhub.common.domain.post.repository.PostRepository;
import com.example.tastyhub.common.domain.user.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    CommentRepository commentRepository;

    @Mock
    PostRepository postRepository;

    @InjectMocks
    CommentServiceImpl commentService;

    @Test
    @DisplayName("댓글 생성")
    void createComment() {
        given(postRepository.findById(any())).willReturn(Optional.ofNullable(POST));
        commentService.createComment(POST.getId(), COMMENT_REQUEST, USER);
        verify(commentRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("댓글 수정")
    void updateComment() {
        given(commentRepository.findById(any())).willReturn(Optional.ofNullable(COMMENT));
        commentService.updateComment(POST.getId(), COMMENT_REQUEST, USER);
        verify(commentRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteComment() {
        given(commentRepository.findById(any())).willReturn(Optional.ofNullable(COMMENT));
        commentService.deleteComment(POST.getId(), USER);
        verify(commentRepository, times(1)).findById(any());
    }

    @Test
    @DisplayName("댓글 생성 실패")
    void createCommentFail() {
        given(postRepository.findById(any())).willThrow(
            new IllegalArgumentException("해당 게시글은 존재하지 않습니다."));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.createComment(POST.getId(), COMMENT_REQUEST, USER);
        });
        assertEquals("해당 게시글은 존재하지 않습니다.",exception.getMessage());

    }

    @Test
    @DisplayName("댓글 수정 실패")
    void updateCommentFail() {
        given(commentRepository.findById(any())).willThrow(
            new IllegalArgumentException("해당 댓글은 존재하지 않습니다."));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.updateComment(POST.getId(), COMMENT_REQUEST, USER);
        });
        assertEquals("해당 댓글은 존재하지 않습니다.",exception.getMessage());
    }

    @Test
    @DisplayName("댓글 삭제 실패")
    void deleteCommentFail() {
        given(commentRepository.findById(any())).willThrow(
            new IllegalArgumentException("해당 댓글은 존재하지 않습니다."));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.deleteComment(POST.getId(), USER);
        });
        assertEquals("해당 댓글은 존재하지 않습니다.",exception.getMessage());
    }
}