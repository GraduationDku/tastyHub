package com.example.tastyhub.common.domain.post.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostUpdateRequest {
    private String title;
    private String text;
}
