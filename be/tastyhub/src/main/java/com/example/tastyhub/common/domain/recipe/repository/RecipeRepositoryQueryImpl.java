package com.example.tastyhub.common.domain.recipe.repository;

import static com.example.tastyhub.common.domain.recipe.entity.QRecipe.recipe;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

import com.example.tastyhub.common.domain.recipe.dtos.PagingRecipeResponse;
import com.querydsl.core.types.Projections;
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
                .select(Projections.constructor(PagingRecipeResponse.class, recipe.id.as("foodid"), recipe.foodName,
                        recipe.foodImgUrl, recipe.foodInformation))
                .from(recipe)
                .orderBy(recipe.createdAt.desc())
                .limit(pageable.getPageSize())
                .fetch();

                long totalSize = countQuery().fetch().get(0);

        return PageableExecutionUtils.getPage(pagingRecipeResponses, pageable, () -> totalSize);
    }

    @Override
    public Page<PagingRecipeResponse> findPopular(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findPopular'");
    }

    @Override
    public Page<PagingRecipeResponse> searchByKeyword(String keyword, Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchByKeyword'");
    }

    private JPAQuery<Long> countQuery() {
        return jpaQueryFactory.select(Wildcard.count)
            .from(recipe);
      }

}
