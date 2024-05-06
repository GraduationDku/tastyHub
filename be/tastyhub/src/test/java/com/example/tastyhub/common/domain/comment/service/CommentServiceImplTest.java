package com.example.tastyhub.common.domain.comment.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.tastyhub.common.domain.comment.repository.CommentRepository;
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

    @InjectMocks
    CommentServiceImpl commentService;

    @Test
    @DisplayName("댓글 생성")
    void createComment() {
    }

    @Test
    @DisplayName("댓글 수정")
    void updateComment() {
    }

    @Test
    @DisplayName("댓글 삭제")
    void deleteComment() {
    }

    @Test
    @DisplayName("댓글 생성 실패")
    void createCommentFail() {
    }

    @Test
    @DisplayName("댓글 수정 실패")
    void updateCommentFail() {
    }

    @Test
    @DisplayName("댓글 삭제 실패")
    void deleteCommentFail() {
    }
}