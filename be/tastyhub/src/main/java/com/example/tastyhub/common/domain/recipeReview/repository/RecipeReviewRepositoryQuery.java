package com.example.tastyhub.common.domain.recipeReview.repository;


import java.util.List;


import com.example.tastyhub.common.domain.recipeReview.dtos.PagingRecipeReviewResponse;

public interface RecipeReviewRepositoryQuery {
        List<PagingRecipeReviewResponse> findAllRecipeReviewResponse(Long recipeId);

}
