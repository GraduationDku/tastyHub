package com.example.tastyhub.common.domain.recipeReview.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import com.example.tastyhub.common.domain.recipe.repository.RecipeRepository;
import com.example.tastyhub.common.domain.recipeReview.dtos.PagingRecipeReviewResponse;
import com.example.tastyhub.common.domain.recipeReview.dtos.RecipeReviewCreateRequest;
import com.example.tastyhub.common.domain.recipeReview.dtos.RecipeReviewUpdateRequest;
import com.example.tastyhub.common.domain.recipeReview.entity.RecipeReview;
import com.example.tastyhub.common.domain.recipeReview.repository.RecipeReviewRepository;
import com.example.tastyhub.common.domain.user.entity.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecipeReviewServiceImpl implements RecipeReviewService {

    private final RecipeReviewRepository recipeReviewRepository;
    private final RecipeRepository recipeRepository;

    @Override
    @Transactional
    public void createRecipeReview(Long recipeId, RecipeReviewCreateRequest recipeReviewCreateRequest, User user) {
        Recipe recipe = recipeRepository.findById(recipeId).get();
        RecipeReview recipeReview = RecipeReview.builder()
                    .user(user)
                    .recipe(recipe)
                    .grade(recipeReviewCreateRequest.getGrade())
                    .text(recipeReviewCreateRequest.getText())
                    .build();
        recipeReviewRepository.save(recipeReview);
    }

    @Override
    public List<PagingRecipeReviewResponse> getRecipeReviews(Long recipeId) {
        return recipeReviewRepository.findAllRecipeReviewResponse(recipeId);
    }

    @Override
    @Transactional
    public void updateRecipeReview(Long recipeReviewId, RecipeReviewUpdateRequest recipeReviewUpdateRequest,
            User user) {
                RecipeReview recipeReview = recipeReviewRepository.findById(recipeReviewId).get();
                recipeReview.update(recipeReviewUpdateRequest);
    }

    @Override
    public void deleteRecipeReview(Long recipeReviewId, User user) {
        recipeReviewRepository.deleteById(recipeReviewId);
    }

}
