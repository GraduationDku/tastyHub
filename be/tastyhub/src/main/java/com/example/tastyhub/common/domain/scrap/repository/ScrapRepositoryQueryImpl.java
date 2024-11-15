package com.example.tastyhub.common.domain.scrap.repository;

import static com.example.tastyhub.common.domain.recipe.entity.QRecipe.recipe;

import java.util.List;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import static com.example.tastyhub.common.domain.scrap.entity.QScrap.scrap;

import com.example.tastyhub.common.domain.scrap.dtos.PagingScrapResponse;
import com.example.tastyhub.common.domain.scrap.dtos.QPagingScrapResponse;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScrapRepositoryQueryImpl implements ScrapRepositoryQuery {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PagingScrapResponse> findScrapedRecipe(Pageable pageable, Long userId) {
        List<PagingScrapResponse> pagingScrapResponses = jpaQueryFactory
                .select(new QPagingScrapResponse(recipe.id, recipe.foodName, recipe.recipeImgUrl))
                .from(scrap)
                .where(scrap.user.id.eq(userId))
                .leftJoin(scrap.recipe, recipe)
                .orderBy(recipe.createdAt.desc())
                .limit(pageable.getPageSize())
                .fetch();
        long totalSize = countQuery().fetch().get(0);

        return PageableExecutionUtils.getPage(pagingScrapResponses, pageable, () -> totalSize);


    }
    private JPAQuery<Long> countQuery() {
        return jpaQueryFactory.select(Wildcard.count)
                .from(scrap);
    }
}
