package com.example.tastyhub.common.domain.recipe.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.tastyhub.common.domain.recipe.dtos.PagingRecipeResponse;

public interface RecipeRepositoryQuery {

    Page<PagingRecipeResponse> findAllandPaging(Pageable pageable);

    Page<PagingRecipeResponse> findMyRecipes(Pageable pageable, Long userId);


    Page<PagingRecipeResponse> findPopular(Pageable pageable);

    Page<PagingRecipeResponse> searchByKeyword(String keyword, Pageable pageable);
}