package com.example.tastyhub.common.domain.recipeReview.service;

import com.example.tastyhub.common.domain.recipe.service.RecipeService;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import com.example.tastyhub.common.domain.recipe.repository.RecipeRepository;
import com.example.tastyhub.common.domain.recipeReview.dtos.PagingMyRecipeReviewResponse;
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
  private final RecipeService recipeService;

  @Override
  @Transactional
  public void createRecipeReview(Long recipeId, RecipeReviewCreateRequest recipeReviewCreateRequest,
      User user) {
    Recipe recipe = recipeService.findById(recipeId);
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
  public void updateRecipeReview(Long recipeReviewId,
      RecipeReviewUpdateRequest recipeReviewUpdateRequest,
      User user) {
    RecipeReview recipeReview = recipeReviewRepository.findById(recipeReviewId)
        .orElseThrow(() -> new IllegalArgumentException("해당 리뷰는 존재하지 않습니다."));
    recipeReview.update(recipeReviewUpdateRequest);
  }

  @Override
  public void deleteRecipeReview(Long recipeReviewId, User user) {
    recipeReviewRepository.deleteById(recipeReviewId);
  }

  @Override
  public List<PagingMyRecipeReviewResponse> getMyRecipeReviews(User user) {
    return recipeReviewRepository.findAllMyRecipeReviewResponse(user.getId());
  }

}
