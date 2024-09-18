package com.example.tastyhub.fixture.recipeReview;

import static com.example.tastyhub.fixture.recipe.RecipeFixture.RECIPE;
import static com.example.tastyhub.fixture.user.UserFixture.USER;

import com.example.tastyhub.common.domain.recipeReview.dtos.PagingMyRecipeReviewResponse;
import com.example.tastyhub.common.domain.recipeReview.dtos.RecipeReviewRequest;
import com.example.tastyhub.common.utils.OrderSpecifierUtil;
import com.querydsl.core.types.OrderSpecifier;
import java.util.List;

import com.example.tastyhub.common.domain.recipeReview.dtos.PagingRecipeReviewResponse;
import com.example.tastyhub.common.domain.recipeReview.entity.RecipeReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class RecipeReviewFixture {
    public static final RecipeReview RECIPE_REVIEW =
        RecipeReview.builder()
            .id(1L)
            .user(USER)
            .recipe(RECIPE)
            .grade(0L)
            .content("text")
            .build();
    public static final RecipeReviewRequest RECIPE_REVIEW_CREATE_REQUEST =
        RecipeReviewRequest.builder()
            .grade(0L)
            .content("text")
            .build();
    public static final RecipeReviewRequest RECIPE_REVIEW_UPDATE_REQUEST =
            RecipeReviewRequest.builder()
                .grade(1L)
                .content("new_text")
                .build();
    public static final PagingRecipeReviewResponse PAGING_RECIPE_REVIEW_RESPONSE = PagingRecipeReviewResponse.builder()
        .grade(0L)
        .nickname(USER.getNickname())
        .content("text")
        .build();

    public static Pageable pageable = PageRequest.of(0, 1);

    public static final Page<PagingRecipeReviewResponse> PAGING_RECIPE_REVIEW_RESPONSES = new PageImpl(List.of(PAGING_RECIPE_REVIEW_RESPONSE),pageable, 1);

    public static final Page<PagingMyRecipeReviewResponse> PAGING_MY_RECIPE_REVIEW_RESPONSES = new PageImpl(List.of(PAGING_RECIPE_REVIEW_RESPONSE),pageable, 1);

    public static OrderSpecifier<?>[] orderSpecifiers = OrderSpecifierUtil.getOrderSpecifiers(pageable, RecipeReview.class, "recipeReview");

}
