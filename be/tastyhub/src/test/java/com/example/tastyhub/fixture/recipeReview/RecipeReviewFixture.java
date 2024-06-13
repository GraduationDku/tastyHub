package com.example.tastyhub.fixture.recipeReview;

import static com.example.tastyhub.fixture.recipe.RecipeFixture.RECIPE;
import static com.example.tastyhub.fixture.user.UserFixture.USER;

import java.util.Collections;
import java.util.List;

import com.example.tastyhub.common.domain.recipeReview.dtos.PagingRecipeReviewResponse;
import com.example.tastyhub.common.domain.recipeReview.dtos.RecipeReviewCreateRequest;
import com.example.tastyhub.common.domain.recipeReview.dtos.RecipeReviewUpdateRequest;
import com.example.tastyhub.common.domain.recipeReview.entity.RecipeReview;

public class RecipeReviewFixture {
    public static final RecipeReview RECIPE_REVIEW =
        RecipeReview.builder()
            .id(1L)
            .user(USER)
            .recipe(RECIPE)
            .grade(0L)
            .text("text")
            .build();
    public static final RecipeReviewCreateRequest RECIPE_REVIEW_CREATE_REQUEST =
        RecipeReviewCreateRequest.builder()
            .grade(0L)
            .text("text")
            .build();
    public static final RecipeReviewUpdateRequest RECIPE_REVIEW_UPDATE_REQUEST =
            RecipeReviewUpdateRequest.builder()
                .grade(1L)
                .text("new_text")
                .build();
    public static final PagingRecipeReviewResponse PAGING_RECIPE_REVIEW_RESPONSE = PagingRecipeReviewResponse.builder()
        .userId(USER.getId())
        .grade(0L)
        .nickname(USER.getNickname())
        .text("text")
        .build();
    public static final List<PagingRecipeReviewResponse> PAGING_RECIPE_REVIEW_RESPONSES = Collections.singletonList(
        PAGING_RECIPE_REVIEW_RESPONSE);
}
