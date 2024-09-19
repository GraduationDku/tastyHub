package com.example.tastyhub.common.domain.recipeReview.service;

import com.example.tastyhub.common.domain.recipe.service.RecipeService;
import com.example.tastyhub.common.domain.recipeReview.dtos.RecipeReviewRequest;
import com.example.tastyhub.common.utils.OrderSpecifierUtil;
import com.querydsl.core.types.OrderSpecifier;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import com.example.tastyhub.common.domain.recipeReview.dtos.PagingMyRecipeReviewResponse;
import com.example.tastyhub.common.domain.recipeReview.dtos.PagingRecipeReviewResponse;
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
  public void createRecipeReview(Long recipeId, RecipeReviewRequest recipeReviewCreateRequest,
      User user) {
    Recipe recipe = recipeService.findById(recipeId);
    RecipeReview recipeReview = RecipeReview.createRecipeReview(recipeReviewCreateRequest, user,
        recipe);
    recipeReviewRepository.save(recipeReview);
  }

  @Override
  public Page<PagingRecipeReviewResponse> getRecipeReviews(Long recipeId, Pageable pageable) {
    OrderSpecifier<?>[] orderSpecifiers = OrderSpecifierUtil.getOrderSpecifiers(pageable,
        RecipeReview.class, "recipeReview");
    return recipeReviewRepository.findAllRecipeReviewResponse(recipeId, pageable, orderSpecifiers);
  }

  @Override
  @Transactional
  public void updateRecipeReview(Long recipeReviewId,
      RecipeReviewRequest recipeReviewUpdateRequest,
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
  public Page<PagingMyRecipeReviewResponse> getMyRecipeReviews(User user, Pageable pageable) {
    OrderSpecifier<?>[] orderSpecifiers = OrderSpecifierUtil.getOrderSpecifiers(pageable,
        RecipeReview.class, "recipeReview");
    return recipeReviewRepository.findAllMyRecipeReviewResponse(user.getId(), pageable,
        orderSpecifiers);
  }

}
