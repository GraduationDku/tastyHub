package com.example.tastyhub.common.domain.recipe.repository;

import static com.example.tastyhub.common.domain.foodInformation.entity.QFoodInformation.foodInformation;
import static com.example.tastyhub.common.domain.recipe.entity.QRecipe.recipe;

import com.querydsl.core.types.Projections;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import com.example.tastyhub.common.domain.recipe.dtos.PagingRecipeResponse;
import com.example.tastyhub.common.domain.recipe.dtos.QPagingRecipeResponse;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecipeRepositoryQueryImpl implements RecipeRepositoryQuery {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PagingRecipeResponse> findAllandPaging(Pageable pageable) {
        List<PagingRecipeResponse> pagingRecipeResponses = jpaQueryFactory
                .select(new QPagingRecipeResponse(recipe.id, recipe.foodName, recipe.foodImgUrl, foodInformation.id,
                        foodInformation.text, foodInformation.cookingTime,foodInformation.serving))
                .from(recipe)
                .leftJoin(recipe.foodInformation, foodInformation)
                .orderBy(recipe.createdAt.desc())
                .limit(pageable.getPageSize())
                .fetch();

        long totalSize = countQuery().fetch().get(0);

        return PageableExecutionUtils.getPage(pagingRecipeResponses, pageable, () -> totalSize);
    }

    @Override
    public Page<PagingRecipeResponse> findMyRecipes(Pageable pageable, Long userId) {
        List<PagingRecipeResponse> pagingRecipeResponses = jpaQueryFactory
                .select(new QPagingRecipeResponse(recipe.id, recipe.foodName, recipe.foodImgUrl, foodInformation.id,
                        foodInformation.text, foodInformation.cookingTime,foodInformation.serving))
                .from(recipe)
                .where(recipe.user.id.eq(userId))
                .leftJoin(recipe.foodInformation, foodInformation)
                .orderBy(recipe.createdAt.desc())
                .limit(pageable.getPageSize())
                .fetch();

        long totalSize = countQuery().fetch().get(0);

        return PageableExecutionUtils.getPage(pagingRecipeResponses, pageable, () -> totalSize);
    }


    @Override
    public Page<PagingRecipeResponse> findPopular(Pageable pageable) {
        List<PagingRecipeResponse> pagingRecipeResponses = jpaQueryFactory
                .select(
                    new QPagingRecipeResponse(recipe.id, recipe.foodName, recipe.foodImgUrl, foodInformation.id,
                        foodInformation.text, foodInformation.cookingTime,foodInformation.serving)
                )
                .from(recipe)
                .leftJoin(recipe.foodInformation, foodInformation)
                .orderBy(recipe.likes.size().desc())
                .limit(7)
                .fetch();

        long totalSize = countQuery().fetch().get(0);

        return PageableExecutionUtils.getPage(pagingRecipeResponses, pageable, () -> totalSize);
    }

    @Override
    public Page<PagingRecipeResponse> searchByKeyword(String keyword, Pageable pageable) {
        List<PagingRecipeResponse> pagingRecipeResponses = jpaQueryFactory
                .select(new QPagingRecipeResponse(recipe.id, recipe.foodName, recipe.foodImgUrl, foodInformation.id,
                        foodInformation.text, foodInformation.cookingTime,foodInformation.serving))
                .from(recipe)
                .where(recipe.foodName.contains(keyword))
                .leftJoin(recipe.foodInformation, foodInformation)
                .orderBy(recipe.createdAt.desc())
                .limit(pageable.getPageSize())
                .fetch();

        long totalSize = countQuery().fetch().get(0);

        return PageableExecutionUtils.getPage(pagingRecipeResponses, pageable, () -> totalSize);
    }

    private JPAQuery<Long> countQuery() {
        return jpaQueryFactory.select(Wildcard.count)
                .from(recipe);
    }

}
