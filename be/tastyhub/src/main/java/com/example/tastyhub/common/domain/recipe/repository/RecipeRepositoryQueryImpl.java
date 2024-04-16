package com.example.tastyhub.common.domain.recipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.tastyhub.common.domain.recipe.dtos.PagingRecipeResponse;

public class RecipeRepositoryQueryImpl implements RecipeRepositoryQuery{

    //private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PagingRecipeResponse> findAllandPaging(Pageable pageable) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAllandPaging'");
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
    
}
