package com.example.tastyhub.common.domain.recipeReview.repository;

import static com.example.tastyhub.common.domain.recipeReview.entity.QRecipeReview.recipeReview;
import static com.example.tastyhub.common.domain.user.entity.QUser.user;

import java.util.List;


import com.example.tastyhub.common.domain.recipeReview.dtos.PagingRecipeReviewResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecipeReviewRepositoryQueryImpl implements RecipeReviewRepositoryQuery{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PagingRecipeReviewResponse> findAllRecipeReviewResponse(Long recipeId) {
        List<PagingRecipeReviewResponse> pagingRecipeReviewResponseList =
             jpaQueryFactory.select(Projections.constructor(PagingRecipeReviewResponse.class,
                    user.id,
                    user.nickname,
                    recipeReview.grade,
                    recipeReview.text
                    ))
                    .from(recipeReview)
                    .where(recipeReview.recipe.id.eq(recipeId))
                    .leftJoin(recipeReview.user,user)
                    .fetch();
        return pagingRecipeReviewResponseList;
    }
}
