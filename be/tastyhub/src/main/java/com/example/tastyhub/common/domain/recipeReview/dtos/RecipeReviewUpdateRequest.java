package com.example.tastyhub.common.domain.recipeReview.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeReviewUpdateRequest {
    private Long grade;
    private String text;
}
