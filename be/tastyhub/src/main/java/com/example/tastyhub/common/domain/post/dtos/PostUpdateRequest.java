package com.example.tastyhub.common.domain.post.dtos;

import com.example.tastyhub.common.domain.post.entity.Post.PostState;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostUpdateRequest {
    private String title;
    private String text;
    private PostState postState;
}
