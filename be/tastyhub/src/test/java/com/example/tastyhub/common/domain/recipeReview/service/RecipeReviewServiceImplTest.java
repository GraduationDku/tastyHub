package com.example.tastyhub.common.domain.recipeReview.service;

import static com.example.tastyhub.fixture.recipeReview.RecipeReviewFixture.RECIPE_REVIEW_CREATE_REQUEST;
import static com.example.tastyhub.fixture.recipeReview.RecipeReviewFixture.RECIPE_REVIEW_UPDATE_REQUEST;
import static com.example.tastyhub.fixture.recipeReview.RecipeReviewFixture.RECIPE_REVIEW;
import static com.example.tastyhub.fixture.recipeReview.RecipeReviewFixture.PAGING_RECIPE_REVIEW_RESPONSES;

import static com.example.tastyhub.fixture.user.UserFixture.USER;
import static com.example.tastyhub.fixture.recipe.RecipeFixture.RECIPE;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.tastyhub.common.domain.recipe.repository.RecipeRepository;
import com.example.tastyhub.common.domain.recipeReview.repository.RecipeReviewRepository;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class RecipeReviewServiceImplTest {
    @Mock
    RecipeReviewRepository recipeReviewRepository;

    @Mock
    RecipeRepository recipeRepository;

    @InjectMocks
    RecipeReviewServiceImpl recipeReviewService;

    @Test
    @DisplayName("레시피 리뷰 생성")
    void createRecipeReview() {
        when(recipeRepository.findById(RECIPE.getId())).thenReturn(Optional.of(RECIPE));
        recipeReviewService.createRecipeReview(USER.getId(), RECIPE_REVIEW_CREATE_REQUEST, USER);
        verify(recipeReviewRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("레시피 리뷰 업데이트")
    void updatePost() {
        given(recipeReviewRepository.findById(any())).willReturn(Optional.ofNullable(RECIPE_REVIEW));
        recipeReviewService.updateRecipeReview(RECIPE_REVIEW.getId(), RECIPE_REVIEW_UPDATE_REQUEST, USER);
        verify(recipeReviewRepository, times(1)).findById(any());

    }
    @Test
    @DisplayName("레시피 리뷰 삭제")
    void deletePost() {
        recipeReviewService.deleteRecipeReview(RECIPE_REVIEW.getId(), USER);
        verify(recipeReviewRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("레시피 리뷰 리스트 조회")
    void getAllPost() {
        given(recipeReviewRepository.findAllRecipeReviewResponse(USER.getId()))
        .willReturn(PAGING_RECIPE_REVIEW_RESPONSES);
        recipeReviewService.getRecipeReviews(USER.getId());
        verify(recipeReviewRepository, times(1)).findAllRecipeReviewResponse(any());
    }
}