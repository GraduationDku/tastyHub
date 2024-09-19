package com.example.tastyhub.common.domain.recipeReview.repository;


import com.querydsl.core.types.OrderSpecifier;
import java.util.List;

import com.example.tastyhub.common.domain.recipeReview.dtos.PagingMyRecipeReviewResponse;
import com.example.tastyhub.common.domain.recipeReview.dtos.PagingRecipeReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeReviewRepositoryQuery {
        Page<PagingRecipeReviewResponse> findAllRecipeReviewResponse(Long recipeId, Pageable pageable, OrderSpecifier<?>[] orderSpecifiers);
        Page<PagingMyRecipeReviewResponse> findAllMyRecipeReviewResponse(Long userId, Pageable pageable, OrderSpecifier<?>[] orderSpecifiers);
}
