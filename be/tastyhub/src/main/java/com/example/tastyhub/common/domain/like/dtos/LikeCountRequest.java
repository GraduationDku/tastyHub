package com.example.tastyhub.common.domain.like.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeCountRequest {
    private Long count;

    public LikeCountRequest createLikeCounetRequest(Long recipeId) {
        return LikeCountRequest.builder().count(recipeId).build();
    }

}
