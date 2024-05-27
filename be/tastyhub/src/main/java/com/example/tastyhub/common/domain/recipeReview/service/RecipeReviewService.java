package com.example.tastyhub.common.domain.recipeReview.service;

import java.util.List;


import com.example.tastyhub.common.domain.recipeReview.dtos.PagingRecipeReviewResponse;
import com.example.tastyhub.common.domain.recipeReview.dtos.RecipeReviewCreateRequest;
import com.example.tastyhub.common.domain.user.entity.User;

public interface RecipeReviewService {

    void createRecipeReview(Long recipeId, RecipeReviewCreateRequest recipeReviewCreateRequest, User user);

    List<PagingRecipeReviewResponse> getRecipeReviews(Long reciped);

}
