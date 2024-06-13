package com.example.tastyhub.fixture.comment;

import static com.example.tastyhub.fixture.post.PostFixture.POST;
import static com.example.tastyhub.fixture.user.UserFixture.USER;

import com.example.tastyhub.common.domain.comment.dtos.CommentDto;
import com.example.tastyhub.common.domain.comment.dtos.CommentRequest;
import com.example.tastyhub.common.domain.comment.entity.Comment;
import java.util.Collections;
import java.util.List;

public class CommentFixture {

    public static final Comment COMMENT = Comment.builder()
        .id(1L)
        .isAlive(Boolean.TRUE)
        .post(POST)
        .user(USER)
        .text("text")
        .build();

    public static final CommentDto COMMENT_DTO =
        CommentDto.builder()
            .userId(1L)
            .nickname("nickname")
            .userImg("ImgUrl")
            .text("comment text")
            .state(true)
            .latestUpdateTime("time")
            .build();
    public static final CommentRequest COMMENT_REQUEST = CommentRequest.builder()
        .text("text").build();

    public static final List<CommentDto> COMMENT_DTOS = Collections.singletonList(COMMENT_DTO);
}
