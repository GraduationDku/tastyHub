package com.example.tastyhub.common.domain.recipe.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.tastyhub.common.domain.recipe.dtos.PagingRecipeResponse;

public interface RecipeService {
// 세현
    Page<PagingRecipeResponse> getAllRecipes(Pageable pageable);

    Page<PagingRecipeResponse> getPopularRecipes(Pageable pageable);

    Page<PagingRecipeResponse> getSearchedRecipes(String foodName, Pageable pageable);
// 세현

}
