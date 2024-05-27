package com.example.tastyhub.common.domain.recipeReview.service;

import com.example.tastyhub.common.domain.recipeReview.dtos.RecipeReviewCreateRequest;
import com.example.tastyhub.common.domain.user.entity.User;

public interface RecipeReviewService {

    void createRecipeReview(Long recipeId, RecipeReviewCreateRequest recipeReviewCreateRequest, User user);

}
