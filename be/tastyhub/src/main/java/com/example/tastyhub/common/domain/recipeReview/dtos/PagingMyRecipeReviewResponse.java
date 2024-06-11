package com.example.tastyhub.common.domain.recipeReview.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagingMyRecipeReviewResponse {
    private Long recipeId;
    private String foodImgUrl;
    private String foodName;
    private Long grade;
    private String text;
}
