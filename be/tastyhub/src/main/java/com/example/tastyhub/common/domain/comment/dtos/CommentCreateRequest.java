package com.example.tastyhub.common.domain.comment.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentCreateRequest {
    private String text;
}
