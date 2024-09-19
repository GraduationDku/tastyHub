package com.example.tastyhub.common.domain.recipe.repository;

import static com.example.tastyhub.common.domain.foodInformation.entity.QFoodInformation.foodInformation;
import static com.example.tastyhub.common.domain.recipe.entity.QRecipe.recipe;
import static com.example.tastyhub.common.domain.like.entity.QLike.like;
import static com.example.tastyhub.common.domain.scrap.entity.QScrap.scrap;


import com.example.tastyhub.common.domain.recipe.entity.Recipe;
import com.example.tastyhub.common.domain.user.entity.User;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
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
        .select(new QPagingRecipeResponse(recipe.id, recipe.foodName, recipe.foodImgUrl,
            foodInformation.id,
            foodInformation.content, foodInformation.cookingTime, foodInformation.serving))
        .from(recipe)
        .leftJoin(recipe.foodInformation, foodInformation)
        .orderBy(getOrderSpecifiers(pageable)) // Dynamic sorting
        .offset(pageable.getOffset()) // Set offset for paging
        .limit(pageable.getPageSize()) // Set page size
        .fetch(); // Fetch the result

    // Count total number of users matching the query
    long totalSize = countQuery().fetchOne();

    return PageableExecutionUtils.getPage(pagingRecipeResponses, pageable, () -> totalSize);

  }

  @Override
  public Page<PagingRecipeResponse> findMyRecipes(Pageable pageable, Long userId) {
    List<PagingRecipeResponse> pagingRecipeResponses = jpaQueryFactory
        .select(new QPagingRecipeResponse(recipe.id, recipe.foodName, recipe.foodImgUrl,
            foodInformation.id,
            foodInformation.content, foodInformation.cookingTime, foodInformation.serving))
        .from(recipe)
        .where(recipe.user.id.eq(userId))
        .leftJoin(recipe.foodInformation, foodInformation)
        .orderBy(getOrderSpecifiers(pageable)) // Dynamic sorting
        .offset(pageable.getOffset()) // Set offset for paging
        .limit(pageable.getPageSize()) // Set page size
        .fetch(); // Fetch the result

    // Count total number of users matching the query
    long totalSize = countQuery().fetchOne();

    return PageableExecutionUtils.getPage(pagingRecipeResponses, pageable, () -> totalSize);


  }


  @Override
  public Page<PagingRecipeResponse> findPopular(Pageable pageable) {
    List<PagingRecipeResponse> pagingRecipeResponses = jpaQueryFactory
        .select(
            new QPagingRecipeResponse(recipe.id, recipe.foodName, recipe.foodImgUrl,
                foodInformation.id,
                foodInformation.content, foodInformation.cookingTime, foodInformation.serving)
        )
        .from(recipe)
        .leftJoin(recipe.foodInformation, foodInformation)
        .orderBy(getOrderSpecifiers(pageable)) // Dynamic sorting
        .offset(pageable.getOffset()) // Set offset for paging
        .limit(pageable.getPageSize()) // Set page size
        .fetch(); // Fetch the result

    // Count total number of users matching the query
    long totalSize = countQuery().fetchOne();

    return PageableExecutionUtils.getPage(pagingRecipeResponses, pageable, () -> totalSize);


  }

  @Override
  public Page<PagingRecipeResponse> searchByKeyword(String keyword, Pageable pageable) {
    List<PagingRecipeResponse> pagingRecipeResponses = jpaQueryFactory
        .select(new QPagingRecipeResponse(recipe.id, recipe.foodName, recipe.foodImgUrl,
            foodInformation.id,
            foodInformation.content, foodInformation.cookingTime, foodInformation.serving))
        .from(recipe)
        .where(recipe.foodName.contains(keyword))
        .leftJoin(recipe.foodInformation, foodInformation)
        .orderBy(getOrderSpecifiers(pageable)) // Dynamic sorting
        .offset(pageable.getOffset()) // Set offset for paging
        .limit(pageable.getPageSize()) // Set page size
        .fetch(); // Fetch the result

    // Count total number of users matching the query
    long totalSize = countQuery().fetchOne();

    return PageableExecutionUtils.getPage(pagingRecipeResponses, pageable, () -> totalSize);
//        return Page.empty();

  }

  private JPAQuery<Long> countQuery() {
    return jpaQueryFactory.select(Wildcard.count)
        .from(recipe);
  }

  @Override
  public boolean isLiked(Long userId, Long recipeId) {
    Long count = jpaQueryFactory
        .select(like.count())
        .from(like)
        .where(
            like.user.id.eq(userId)
                .and(like.recipe.id.eq(recipeId))
        )
        .fetchOne();

    return count != null && count > 0;

  }

  @Override
  public boolean isScraped(Long userId, Long recipeId) {
    Long count = jpaQueryFactory
        .select(scrap.count())
        .from(scrap)
        .where(
            scrap.user.id.eq(userId)
                .and(scrap.recipe.id.eq(recipeId))
        )
        .fetchOne();

    return count != null && count > 0;
  }



  // Helper method to dynamically add sort specifiers with direction
  private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable) {
    return pageable.getSort().stream()
        .map(order -> {
          PathBuilder pathBuilder = new PathBuilder<>(Recipe.class, "recipe");
          // Determine sorting direction (ascending or descending)
          return new OrderSpecifier<>(
              order.isAscending() ? Order.ASC : Order.DESC,
              pathBuilder.get(order.getProperty())); // Use the property name for sorting
        })
        .toArray(OrderSpecifier[]::new);
  }

}
