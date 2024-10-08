package com.example.tastyhub.common.domain.recipe.repository;

import com.example.tastyhub.common.utils.page.RestPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.tastyhub.common.domain.recipe.dtos.PagingRecipeResponse;

public interface RecipeRepositoryQuery {

    Page<PagingRecipeResponse> findAllandPaging(Pageable pageable);

    Page<PagingRecipeResponse> findMyRecipes(Pageable pageable, Long userId);


    RestPage<PagingRecipeResponse> findPopular(Pageable pageable);

    Page<PagingRecipeResponse> searchByKeyword(String keyword, Pageable pageable);

    boolean isLiked(Long userId, Long recipeId);
    
    boolean isScraped(Long userId, Long recipeId);
}