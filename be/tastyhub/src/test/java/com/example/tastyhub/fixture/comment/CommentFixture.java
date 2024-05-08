package com.example.tastyhub.fixture.comment;

import static com.example.tastyhub.fixture.post.PostFixture.POST;
import static com.example.tastyhub.fixture.user.UserFixture.USER;

import com.example.tastyhub.common.domain.comment.dtos.CommentDto;
import com.example.tastyhub.common.domain.comment.dtos.CommentRequest;
import com.example.tastyhub.common.domain.comment.entity.Comment;

public class CommentFixture {

    public static final Comment COMMENT = Comment.builder()
        .id(1L)
        .isAlive(Boolean.TRUE)
        .post(POST)
        .user(USER)
        .text("text")
        .build();

    public static final CommentDto COMMENT_DTO = CommentDto.builder()
        .latestUpdateTime("time")
        .nickname("nickname")
        .state(Boolean.TRUE)
        .userId(1L)
        .userImg("ImgUrl")
        .build();

    public static final CommentRequest COMMENT_REQUEST = CommentRequest.builder()
        .text("text").build();
}
