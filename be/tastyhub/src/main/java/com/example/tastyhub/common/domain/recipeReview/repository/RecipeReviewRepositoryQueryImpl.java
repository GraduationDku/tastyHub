package com.example.tastyhub.common.domain.recipeReview.repository;

import static com.example.tastyhub.common.domain.recipeReview.entity.QRecipeReview.recipeReview;
import static com.example.tastyhub.common.domain.user.entity.QUser.user;
import static com.example.tastyhub.common.domain.recipe.entity.QRecipe.recipe;

import com.querydsl.core.types.OrderSpecifier;
import java.util.List;

import com.example.tastyhub.common.domain.recipeReview.dtos.PagingMyRecipeReviewResponse;
import com.example.tastyhub.common.domain.recipeReview.dtos.PagingRecipeReviewResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class RecipeReviewRepositoryQueryImpl implements RecipeReviewRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PagingRecipeReviewResponse> findAllRecipeReviewResponse(Long recipeId,
        Pageable pageable, OrderSpecifier<?>[] orderSpecifiers) {
        List<PagingRecipeReviewResponse> pagingRecipeReviewResponseList =
            jpaQueryFactory.select(Projections.fields(
                    PagingRecipeReviewResponse.class,
                    user.nickname.as("nickname"),  // 필드 이름 매핑
                    recipeReview.grade.as("grade"),
                    recipeReview.content.as("content")
                ))
                .from(recipeReview)
                .where(recipeReview.recipe.id.eq(recipeId))
                .leftJoin(recipeReview.user, user)
                .orderBy(orderSpecifiers)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalSize = pagingRecipeReviewResponseList.size();
        return PageableExecutionUtils.getPage(pagingRecipeReviewResponseList, pageable,
            () -> totalSize);
    }


    @Override
    public Page<PagingMyRecipeReviewResponse> findAllMyRecipeReviewResponse(Long userId,
        Pageable pageable, OrderSpecifier<?>[] orderSpecifiers) {
        List<PagingMyRecipeReviewResponse> pagingMyRecipeReviewResponseList =
            jpaQueryFactory.select(Projections.fields(
                    PagingMyRecipeReviewResponse.class,
                    recipe.id.as("recipeId"),
                    recipe.recipeImgUrl.as("recipeImgUrl"),
                    recipe.foodName.as("foodName"),
                    recipeReview.grade.as("grade"),
                    recipeReview.content.as("content")
                ))
                .from(recipeReview)
                .where(recipeReview.user.id.eq(userId))
                .leftJoin(recipeReview.recipe, recipe)
                .orderBy(orderSpecifiers)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long totalSize = pagingMyRecipeReviewResponseList.size();
        return PageableExecutionUtils.getPage(pagingMyRecipeReviewResponseList, pageable,
            () -> totalSize);
    }

}