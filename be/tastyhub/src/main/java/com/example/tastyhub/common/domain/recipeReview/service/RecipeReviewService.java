package com.example.tastyhub.common.domain.recipeReview.service;

import com.example.tastyhub.common.domain.recipeReview.dtos.PagingMyRecipeReviewResponse;
import com.example.tastyhub.common.domain.recipeReview.dtos.PagingRecipeReviewResponse;
import com.example.tastyhub.common.domain.recipeReview.dtos.RecipeReviewRequest;
import com.example.tastyhub.common.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeReviewService {

    void createRecipeReview(Long recipeId, RecipeReviewRequest recipeReviewCreateRequest, User user);

    Page<PagingRecipeReviewResponse> getRecipeReviews(Long reciped, Pageable pageable);

    void updateRecipeReview(Long recipeReviewId, RecipeReviewRequest recipeReviewUpdateRequest, User user);

    void deleteRecipeReview(Long recipeReviewId, User user);

    Page<PagingMyRecipeReviewResponse> getMyRecipeReviews(User user, Pageable pageable);

}
